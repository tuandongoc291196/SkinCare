package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Account;
import com.fu.skincare.entity.UserTestResult;

@Repository
public interface UserTestResultRepository extends JpaRepository<UserTestResult, Integer> {

    int countByAccount(Account account);

    List<UserTestResult> findByAccountOrderByIdDesc(Account account);

    List<UserTestResult> findAllByOrderByIdDesc();

}
