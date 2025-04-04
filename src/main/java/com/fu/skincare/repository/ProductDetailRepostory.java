package com.fu.skincare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.ProductDetail;

@Repository
public interface ProductDetailRepostory extends JpaRepository<ProductDetail, Integer> {
    
}
