package com.usyd.edugenie.controller;

import com.usyd.edugenie.entity.Quizzes;
import com.usyd.edugenie.entity.Questions;
import com.usyd.edugenie.entity.UserGeneratedStudyNotes;
import com.usyd.edugenie.entity.Users;
import com.usyd.edugenie.entity.Tag;
import com.usyd.edugenie.model.TagScore;
import com.usyd.edugenie.service.QuizzesService;
import com.usyd.edugenie.service.QuestionsService;
import com.usyd.edugenie.service.TagService;
import com.usyd.edugenie.service.UserGeneratedStudyNotesService;
import com.usyd.edugenie.repository.UsersRepository;
import com.usyd.edugenie.OpenAIMss.ChatGPTRequest;
import com.usyd.edugenie.OpenAIMss.ChatGPTResponse;
import com.usyd.edugenie.model.QuizGenerateReq;
import com.usyd.edugenie.model.TagsReq;

import com.fasterxml.jackson.core.JsonProcessingException; // Added import for JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @Autowired
    private QuizzesService quizzesService;

    @Autowired
    private QuestionsService questionsService;

    @Autowired
    private UserGeneratedStudyNotesService studyNotesService;

    @Autowired
    private UsersRepository userRepository;  // Inject the UsersRepository

    @Autowired
    private TagService tagService;


    @PostMapping("/generate")
    public ResponseEntity<List<Questions>> generateQuiz(@RequestBody QuizGenerateReq quizRequest) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Users user = (Users) authentication.getPrincipal();

            JsonNode questionsNode = null;

            String studyNotes = quizRequest.getStudyNotes();
            String topic = quizRequest.getTopic();
            int numberOfQuestions = quizRequest.getNumberOfQuestions();

            if (studyNotes != null && !studyNotes.isEmpty()) {
                topic = generateTopicFromStudyNotes(studyNotes);
                questionsNode = getGeneratedQuestionsFromOpenAIUsingNotes(studyNotes, numberOfQuestions);
                saveUserGeneratedStudyNotes(studyNotes);
            } else if (topic != null && !topic.isEmpty()) {
                questionsNode = getGeneratedQuestionsFromOpenAI(topic, numberOfQuestions);
            }

            if (questionsNode != null && questionsNode.isArray() && questionsNode.size() > 0) {
                Quizzes newQuiz = saveQuiz(numberOfQuestions, topic, user);
                List<Questions> questionsList = saveQuestions(newQuiz, questionsNode);
                return new ResponseEntity<>(questionsList, HttpStatus.OK);
            }

            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/tags")
    public List<String> generateTags(@RequestBody TagsReq tagsReq) {
        String topic = tagsReq.getTopic();
        String studyNotes = tagsReq.getStudyNotes();
        UUID quizId = tagsReq.getQuizId();

        String prompt = "Please assign one or more suitable tags strictly from the following list:" +
                "[Data Science, Business, Computer Science, Language Learning, Health, Personal Development, Physical Science and Engineering, Social, Sciences, Arts and Humanities, Math and Logic]." +
                "No any other tags out of the list above. Please strictly separate them with an English comma without the word Tags: at the beginning and without dashes or any other extra texts";
        
        if (studyNotes != null && !studyNotes.isEmpty()) {
            prompt += "Assign for the following study notes: \n " + studyNotes + "\n" + prompt;
        } else {
            prompt += "Assign for the topic: " + topic;
        }

        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        try {
            ChatGPTResponse chatGptResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
            if (chatGptResponse != null && chatGptResponse.getChoices() != null && !chatGptResponse.getChoices().isEmpty()) {
                String content = chatGptResponse.getChoices().get(0).getMessage().getContent();

                List<String> tags = Arrays.stream(content.split(","))
                        .map(String::trim)
                        .collect(Collectors.toList());

                // Save the generated tags to the database
                saveGeneratedTags(tags, quizId);

                return tags;
            } else {
                return Collections.singletonList("No tags generated");
            }
        } catch (Exception e) {
            return Collections.singletonList("Error generating tags: " + e.getMessage());
        }
}


    private void saveGeneratedTags(List<String> tags, UUID quizId) {
        for (String tagName : tags) {
            UUID tagId = null;
            final Optional<Tag> tagByName = tagService.getTagByName(tagName);
            if (tagByName.isPresent()) {
                tagId = tagByName.get().getTagId();
            } else {
                Tag tag = new Tag(UUID.randomUUID(), "Data Science");
                tag.setName(tagName);
                tagService.createTag(tag);
                tagId = tag.getTagId();
            }

            quizzesService.addTagToQuiz(quizId, tagId);
        }
    }

    private UserGeneratedStudyNotes saveUserGeneratedStudyNotes(String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) authentication.getPrincipal();
        UserGeneratedStudyNotes studyNotes = new UserGeneratedStudyNotes();
        studyNotes.setUser(user);
        studyNotes.setContent(content);
        studyNotes.setUploadDate(LocalDateTime.now());

        System.out.println("Saving study notes content: " + content);

        return studyNotesService.createUserGeneratedStudyNotes(studyNotes);
    }

    private JsonNode getGeneratedQuestionsFromOpenAI(String input, int numberOfQuestions) throws Exception {
        String prompt = "Generate " + numberOfQuestions + " multiple-choice (please exclude lettering a, b, c, d) questions based on the following topic: " + input + ". Format the response as a JSON object with 'questions' as an array of objects. Each object should have 'text' for the question, 'options', an array of 4 answer options, and 'correct', the correct answer.";
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);

        ChatGPTResponse chatGptResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
        if (chatGptResponse != null && chatGptResponse.getChoices() != null && !chatGptResponse.getChoices().isEmpty()) {
            String jsonResponse = chatGptResponse.getChoices().get(0).getMessage().getContent();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(jsonResponse).path("questions");
        }
        return null;
    }

    private JsonNode getGeneratedQuestionsFromOpenAIUsingNotes(String input, int numberOfQuestions) throws Exception {
        String prompt = "Generate " + numberOfQuestions + " multiple-choice questions (please exclude lettering a, b, c, d) based on the following study notes: " + input + ". Format the response as a JSON object with 'questions' as an array of objects. Each object should have 'text' for the question, 'options', an array of 4 answer options, and 'correct', the correct answer.";
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);

        ChatGPTResponse chatGptResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
        if (chatGptResponse != null && chatGptResponse.getChoices() != null && !chatGptResponse.getChoices().isEmpty()) {
            String jsonResponse = chatGptResponse.getChoices().get(0).getMessage().getContent();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(jsonResponse).path("questions");
        }
        return null;
    }

    // this function generates a topic for a quiz if the user uses study notes to generate the quiz
    private String generateTopicFromStudyNotes(String input) throws Exception {
        String prompt = "Based on the following study notes, generate a short, simple and suitable topic or title: " + input + ". Emphasis on short.";
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);

        ChatGPTResponse chatGptResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
        if (chatGptResponse != null && chatGptResponse.getChoices() != null && !chatGptResponse.getChoices().isEmpty()) {
            return chatGptResponse.getChoices().get(0).getMessage().getContent().trim();
        }
        return "Generated from Study Notes"; // if generating a topic does not work, this is the default
    }

    private Quizzes saveQuiz(int numberOfQuestions, String topic, Users user) {
        Quizzes newQuiz = new Quizzes();
        newQuiz.setTotalQuestions(numberOfQuestions);
        newQuiz.setGeneratedDate(LocalDateTime.now());
        newQuiz.setLastAttemptDate(null);
        newQuiz.setScore(0);
        newQuiz.setTopic(topic);
        newQuiz.setUser(user);
        return quizzesService.createQuiz(newQuiz);
    }

    private List<Questions> saveQuestions(Quizzes quiz, JsonNode questionsNode) {
        List<Questions> savedQuestions = new ArrayList<>();

        for (JsonNode questionNode : questionsNode) {
            String questionText = questionNode.get("text").asText();
            String correctAnswerText = questionNode.get("correct").asText();
            List<String> options = new ArrayList<>();
            questionNode.get("options").forEach(optionNode -> options.add(optionNode.asText()));

            // Create and save the question
            Questions question = new Questions();
            question.setQuiz(quiz);
            question.setQuestionText(questionText);
            question.setOptions(options);
            question.setCorrectAnswer(correctAnswerText);

            questionsService.saveQuestion(question);
            savedQuestions.add(question);
        }

        return savedQuestions;
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitQuiz(@RequestParam int score) {
        // Retrieve the most recent quiz of user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) authentication.getPrincipal();
        List<Quizzes> quizOptional = quizzesService.getQuizzesByUserSortedByDate(user);
        if (quizOptional.isEmpty()) {
            return new ResponseEntity<>("No quiz found", HttpStatus.NOT_FOUND);
        }

        Quizzes quiz = quizOptional.get(0);

        // Update the quiz with the received score and the last attempt date
        quiz.setScore(score);
        quiz.setLastAttemptDate(LocalDateTime.now());
        quizzesService.createQuiz(quiz);  // Update the quiz in the database

        return new ResponseEntity<>("Quiz submitted successfully. Your score is: " + score, HttpStatus.OK);
    }

    @PostMapping("/feedback")
    public ResponseEntity<String> getFeedback(@RequestBody List<String> incorrectQuestions) {

        String feedback = generateFeedback(incorrectQuestions);

        Optional<Quizzes> quizOptional = quizzesService.getMostRecentQuiz();

        Quizzes quiz = quizOptional.get();

        // Update the quiz with the received score and the last attempt date
        List<String> feedbackList = Arrays.asList(feedback.split("\n"));

        String short_feedback = feedback;
        if (feedback.length() > 1000) {
            short_feedback = feedback.substring(0, 1000);
        }
        System.out.println("Feedback: " + feedback);

        quiz.setFeedback(short_feedback);
        quizzesService.createQuiz(quiz); 

        return ResponseEntity.ok(feedback);
    }

     private String generateFeedback(List<String> incorrectQuestions) {
        String prompt = "Give suggestions on how to improve understanding of the following questions, " + incorrectQuestions.toString();
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);

        ChatGPTResponse chatGptResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
        if (chatGptResponse != null && !chatGptResponse.getChoices().isEmpty()) {
            return chatGptResponse.getChoices().get(0).getMessage().getContent();
        }
        return "No feedback available.";
    }



    @GetMapping("/{quizId}")
    public ResponseEntity<Map<String, Object>> getQuizById(@PathVariable UUID quizId) {
        Optional<Quizzes> quizOptional = quizzesService.getQuizById(quizId);

        if (quizOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Quizzes quizEntity = quizOptional.get();
        List<Questions> questions = questionsService.getQuestionsByQuizId(quizId);

        // put quiz and questions in the response entity
        Map<String, Object> response = new HashMap<>();
        response.put("quiz", quizEntity);
        response.put("questions", questions);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/tagscores")
    public ResponseEntity<List<TagScore>> getTagScoresForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) authentication.getPrincipal();

        List<TagScore> tagScores = quizzesService.getTagScoreForUser(user);
        return new ResponseEntity<>(tagScores, HttpStatus.OK);
    }
}
