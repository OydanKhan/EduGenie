package com.usyd.edugenie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import com.usyd.edugenie.entity.Quizzes;
import com.usyd.edugenie.entity.Tag;
import com.usyd.edugenie.entity.QuizTags;
import com.usyd.edugenie.entity.QuizTagsId;

import java.util.UUID;
import java.util.List;

@Repository
public interface QuizTagsRepository extends JpaRepository<QuizTags, QuizTagsId> {

    // will find tags for a given quizid
    @Query("SELECT qt.tag FROM QuizTags qt WHERE qt.quizId = :quizId")
    List<Tag> findTagsByQuizId(UUID quizId);

    // Will find tag names for a given quizId
    @Query("SELECT t.name FROM QuizTags qt JOIN qt.tag t WHERE qt.quizId = :quizId")
    List<String> findTagNamesByQuizId(UUID quizId);
}