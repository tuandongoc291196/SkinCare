package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
  List<Brand> findByStatus(String status);
}
