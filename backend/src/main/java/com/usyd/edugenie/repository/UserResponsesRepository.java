package com.usyd.edugenie.repository;

import com.usyd.edugenie.entity.UserResponses;
import com.usyd.edugenie.entity.Quizzes;
import com.usyd.edugenie.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface UserResponsesRepository extends JpaRepository<UserResponses, UUID> {
    Optional<UserResponses> findByQuizAndQuestion(Quizzes quiz, Questions question);
}
