package com.fu.skincare.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Brand;
import com.fu.skincare.entity.Category;
import com.fu.skincare.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
  Page<Product> findAllByStatus(String status, Pageable pageable);

  Page<Product> findAllByBrandAndStatus(Brand brand, String status, Pageable pageable);

  Page<Product> findAllByCategoryAndStatus(Category category, String status, Pageable pageable);
}
