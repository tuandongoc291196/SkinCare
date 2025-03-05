package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {
  List<Position> findByStatus(String status);
}
