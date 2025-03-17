package com.fu.skincare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.UserSkinType;
import com.fu.skincare.entity.UserTestResult;

@Repository
public interface UserSkinTypeRepository extends JpaRepository<UserSkinType, Integer> {
    UserSkinType findFirstByUserTestResult(UserTestResult userTestResult);
}
