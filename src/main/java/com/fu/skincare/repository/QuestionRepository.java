package com.fu.skincare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Page<Question> findAllByStatus(String status, Pageable pageable);
}
