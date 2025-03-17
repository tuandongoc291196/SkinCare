package com.fu.skincare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.SkinType;


@Repository
public interface SkinTypeRepository extends JpaRepository<SkinType, Integer> {
    SkinType findByType(String type);
}
