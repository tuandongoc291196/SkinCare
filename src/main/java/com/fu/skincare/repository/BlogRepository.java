package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    List<Blog> findByStatus(String status);    
}
