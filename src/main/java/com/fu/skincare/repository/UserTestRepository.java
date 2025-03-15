package com.fu.skincare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.UserTest;

@Repository
public interface UserTestRepository extends JpaRepository<UserTest, Integer>{
    
}
