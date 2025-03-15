package com.fu.skincare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.UserSkinType;

@Repository
public interface UserSkinTypeRepository extends JpaRepository<UserSkinType, Integer> {
    
}
