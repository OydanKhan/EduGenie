package com.usyd.edugenie.controller;

import com.usyd.edugenie.OpenAIMss.ChatGPTRequest;
import com.usyd.edugenie.OpenAIMss.ChatGPTResponse;
import com.usyd.edugenie.entity.*;
import com.usyd.edugenie.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.usyd.edugenie.service.StudyNotesService;

@RestController
@RequestMapping("/learn")
public class CourseNotesGenController {
    @Value("${openai.model}")
    private String model;
    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;
    @Autowired
    private StudyNotesService studyNotesService;
    @Autowired
    private TagService tagService;

    @GetMapping("/course")
    public ResponseEntity<StudyNotes> generateCourse(@RequestParam("topic") String topic) {
        String prompt = "I want to learn " + topic + ". Please introduce how to learn in detail.";
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);

        try {
            ChatGPTResponse chatGptResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
            if (chatGptResponse != null && chatGptResponse.getChoices() != null && !chatGptResponse.getChoices().isEmpty()) {
                String course = chatGptResponse.getChoices().get(0).getMessage().getContent();
                String downloadLink = generatePdfLink(course, topic);
                StudyNotes studyNotes = saveGeneratedCourse(topic, course, downloadLink);
                return ResponseEntity.ok(studyNotes);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private String generatePdfLink(String content, String topic) {
        // Code to generate PDF link omitted for brevity
        return "http://localhost:8088/files/" + topic + ".pdf";
    }

    private StudyNotes saveGeneratedCourse(String topic, String course, String filepath) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) authentication.getPrincipal();

        StudyNotes studyNotes = new StudyNotes();
        studyNotes.setUser(user);
        studyNotes.setContent(course);
        studyNotes.setTopic(topic);
        studyNotes.setTitle(topic);
        studyNotes.setDownloadFile(filepath);
        studyNotes.setGeneratedDate(LocalDateTime.now());

        return studyNotesService.createStudyNote(studyNotes);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> generateTags(@RequestParam("topic") String topic, @RequestParam("courseId") String courseId) {
        String prompt = "Please assign one or more suitable tags for " + topic + " from the following list: " +
            "[Data Science, Business, Computer Science, Language Learning, Health, Personal Development, " +
            "Physical Science and Engineering, Social, Sciences, Arts and Humanities, Math and Logic]";
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);

        try {
            ChatGPTResponse chatGptResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
            if (chatGptResponse != null && chatGptResponse.getChoices() != null && !chatGptResponse.getChoices().isEmpty()) {
                String content = chatGptResponse.getChoices().get(0).getMessage().getContent();
                List<String> tags = Arrays.asList(content.split(","));
                List<Tag> courseTags = saveTags(tags);
                saveCourseTags(courseId, courseTags);
                return new ResponseEntity<>(courseTags, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void saveCourseTags(String courseId, List<Tag> tags) {
        for (Tag tagName : tags) {
            StudyNotesTags courseTag = new StudyNotesTags();
            UUID courseUd = UUID.fromString(courseId);
            courseTag.setTag(tagName);
            courseTag.setNoteId(courseUd);
            courseTag.setTagId(tagName.getTagId());
            tagService.createCourseTag(courseTag);
        }
    }

    private List<Tag> saveTags(List<String> tags) {
        List<Tag> studyNotesTagsList = new ArrayList<>();
        for (String tagName : tags) {
            Tag tag = new Tag(UUID.randomUUID(), tagName.trim()); // Only change needed for unique tags
            studyNotesTagsList.add(tagService.createTag(tag));
        }
        return studyNotesTagsList;
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<Map<String, Object>> getNoteById(@PathVariable UUID noteId) {
        // Find the note by its ID
        Optional<StudyNotes> noteOptional = studyNotesService.getStudyNoteById(noteId);

        // If the note is not found, return a 404 Not Found status
        if (noteOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        StudyNotes note = noteOptional.get(); // Get the note if present

        Map<String, Object> response = new HashMap<>();
        response.put("noteId", note.getNoteId());
        response.put("title", note.getTitle());
        response.put("topic", note.getTopic());
        response.put("content", note.getContent());
        response.put("generatedDate", note.getGeneratedDate());
        response.put("downloadFile", note.getDownloadFile());

        // Retrieve the tags
        List<Tag> tags = studyNotesService.getTagsForStudyNote(noteId);
        List<String> tagNames = tags.stream()
            .map(Tag::getName)
            .collect(Collectors.toList());
        response.put("tags", tagNames);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

