package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    @Query(value = "SELECT p.id as id, p.name as name, SUM(od.quantity) AS quantity \n" + //
            "FROM order_detail od \n" + //
            "JOIN product p ON od.product_id = p.id \n" + //
            "GROUP BY p.id, p.name \n" + //
            "ORDER BY quantity DESC \n" + //
            "LIMIT 5;", nativeQuery = true)
    public List<Object[]> getOrderDetailReport();
}
