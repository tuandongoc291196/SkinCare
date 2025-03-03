package com.fu.skincare.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    // Add custom query methods if needed
    Optional<Account> findAccountByEmail(String email);
}