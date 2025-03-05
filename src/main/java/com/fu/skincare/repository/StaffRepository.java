package com.fu.skincare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.skincare.entity.Staff;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

}
