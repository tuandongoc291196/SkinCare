package com.fu.skincare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.ProductSkinType;

@Repository
public interface ProductSkinTypeRepository extends JpaRepository<ProductSkinType, Integer> {
    
}
