package com.usyd.edugenie.repository;

import com.usyd.edugenie.entity.Quizzes;
import com.usyd.edugenie.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

public interface QuizzesRepository extends JpaRepository<Quizzes, UUID> {
    Optional<Quizzes> findTopByOrderByGeneratedDateDesc();
    List<Quizzes> findByUser(Users user);
    List<Quizzes> findByUserOrderByGeneratedDateDesc(Users user);
}
