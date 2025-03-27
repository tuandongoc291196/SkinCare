package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.ProductSkinType;
import com.fu.skincare.entity.SkinType;

@Repository
public interface ProductSkinTypeRepository extends JpaRepository<ProductSkinType, Integer> {
  List<ProductSkinType> findBySkinType(SkinType skinType);
}
