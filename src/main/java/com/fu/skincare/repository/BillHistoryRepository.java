package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Bill;
import com.fu.skincare.entity.BillHistory;

@Repository
public interface BillHistoryRepository extends JpaRepository<BillHistory, Integer> {
  List<BillHistory> findByBill(Bill bill);
}
