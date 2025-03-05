package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
  List<Category> findByStatus(String status);
}
