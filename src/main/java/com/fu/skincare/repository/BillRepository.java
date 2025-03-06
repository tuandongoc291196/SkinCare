package com.fu.skincare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

}
