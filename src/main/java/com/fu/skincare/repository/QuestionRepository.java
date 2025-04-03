package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Page<Question> findAllByStatus(String status, Pageable pageable);

    @Query(value = "SELECT q.* FROM (\n" + //
            "    SELECT id FROM question\n" + //
            "    WHERE status = 'ACTIVATED'\n" + //
            "    ORDER BY RAND()\n" + //
            "    LIMIT 20\n" + //
            ") AS r\n" + //
            "JOIN question AS q ON q.id = r.id;", nativeQuery = true)
    List<Question> getQuestion();
}
