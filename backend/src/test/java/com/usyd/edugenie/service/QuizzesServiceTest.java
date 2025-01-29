package com.usyd.edugenie.service;

import com.usyd.edugenie.entity.Quizzes;
import com.usyd.edugenie.entity.Tag;
import com.usyd.edugenie.entity.Users;
import com.usyd.edugenie.model.TagScore;
import com.usyd.edugenie.repository.QuizzesRepository;
import com.usyd.edugenie.repository.QuizTagsRepository;
import com.usyd.edugenie.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class QuizzesServiceTest {

    @InjectMocks
    private QuizzesService quizzesService;

    @Mock
    private QuizzesRepository quizzesRepository;

    @Mock
    private QuizTagsRepository quizTagsRepository;

    @Mock
    private TagRepository tagRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateQuiz() {
        Quizzes quiz = new Quizzes();
        quiz.setQuizId(UUID.randomUUID());
        quiz.setGeneratedDate(LocalDateTime.now());

        when(quizzesRepository.save(any(Quizzes.class))).thenReturn(quiz);

        Quizzes createdQuiz = quizzesService.createQuiz(quiz);
        assertEquals(quiz.getQuizId(), createdQuiz.getQuizId());
        verify(quizzesRepository, times(1)).save(quiz);
    }

    @Test
    public void testGetQuizById() {
        UUID quizId = UUID.randomUUID();
        Quizzes quiz = new Quizzes();
        quiz.setQuizId(quizId);

        when(quizzesRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        Optional<Quizzes> retrievedQuiz = quizzesService.getQuizById(quizId);
        assertTrue(retrievedQuiz.isPresent());
        assertEquals(quizId, retrievedQuiz.get().getQuizId());
        verify(quizzesRepository, times(1)).findById(quizId);
    }

    @Test
    public void testDeleteQuiz() {
        UUID quizId = UUID.randomUUID();
        quizzesService.deleteQuiz(quizId);
        verify(quizzesRepository, times(1)).deleteById(quizId);
    }

    @Test
    public void testGetQuizzesByUser() {
        Users user = new Users();
        user.setUserId(UUID.randomUUID());
        Quizzes quiz = new Quizzes();
        quiz.setUser(user);

        when(quizzesRepository.findByUser(user)).thenReturn(Collections.singletonList(quiz));

        List<Quizzes> quizzes = quizzesService.getQuizzesByUser(user);
        assertEquals(1, quizzes.size());
        assertEquals(user.getUserId(), quizzes.get(0).getUser().getUserId());
        verify(quizzesRepository, times(1)).findByUser(user);
    }

    @Test
    public void testAddTagToQuiz() {
        UUID quizId = UUID.randomUUID();
        UUID tagId = UUID.randomUUID();

        Quizzes quiz = new Quizzes();
        Tag tag = new Tag();

        when(quizzesRepository.findById(quizId)).thenReturn(Optional.of(quiz));
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        quizzesService.addTagToQuiz(quizId, tagId);

        verify(quizTagsRepository, times(1)).save(any());
    }

    @Test
    public void testGetTagsForUserQuizzes() {
        Users user = new Users();
        Quizzes quiz = new Quizzes();
        quiz.setQuizId(UUID.randomUUID());

        when(quizzesRepository.findByUser(user)).thenReturn(Collections.singletonList(quiz));
        when(quizTagsRepository.findTagNamesByQuizId(quiz.getQuizId())).thenReturn(Collections.singletonList("Data Science"));

        Set<String> tags = quizzesService.getTagsForUserQuizzes(user);
        assertEquals(1, tags.size());
        assertTrue(tags.contains("Data Science"));
    }

    @Test
    public void testGetTagScoreForUser() {
        Users user = new Users();
        user.setUserId(UUID.randomUUID());

        Quizzes quiz = new Quizzes();
        quiz.setQuizId(UUID.randomUUID());
        quiz.setScore(8);
        quiz.setTotalQuestions(10);
        quiz.setLastAttemptDate(LocalDateTime.now());

        when(quizzesRepository.findByUser(user)).thenReturn(Collections.singletonList(quiz));
        when(quizTagsRepository.findTagNamesByQuizId(quiz.getQuizId())).thenReturn(Collections.singletonList("Data Science"));

        List<TagScore> tagScores = quizzesService.getTagScoreForUser(user);

        assertEquals(1, tagScores.size());
        assertEquals("Data Science", tagScores.get(0).getTag());
        assertEquals(80, tagScores.get(0).getAvgScore());
    }
}
