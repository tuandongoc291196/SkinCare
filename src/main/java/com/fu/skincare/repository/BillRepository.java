package com.fu.skincare.repository;

import com.fu.skincare.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    Page<Bill> findAllByStatus(String status, Pageable pageable);

    Page<Bill> findAllByAccount(Account account, Pageable pageable);
}
