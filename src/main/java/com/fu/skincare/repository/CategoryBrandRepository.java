package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Brand;
import com.fu.skincare.entity.Category;
import com.fu.skincare.entity.CategoryBrand;

@Repository
public interface CategoryBrandRepository extends JpaRepository<CategoryBrand, Integer> {
    CategoryBrand findByCategoryAndBrand(Category category, Brand brand);

    List<CategoryBrand> findByCategory(Category category);

    List<CategoryBrand> findByBrand(Brand brand);
}
