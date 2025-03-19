package com.fu.skincare.response.transaction;

import com.fu.skincare.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("select t from Transaction t where t.bill.id = ?1")
    Optional<Transaction> findByBillId(int billId);
}
