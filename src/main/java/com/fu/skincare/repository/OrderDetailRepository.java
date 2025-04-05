package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    @Query(value = "select p.id, p.name, sum(od.quantity) as quantity\n" + //
            "   from product p \n" + //
            "       join product_detail pd on pd.product_id  = p.id\n" + //
            "       join order_detail od on od.product_detail_id  = pd.id \n" + //
            "   where od.status like 'SUCCESS' or od.status like 'DONE' \n" + //
            "   GROUP BY p.id, p.name \n" + //
            "   ORDER BY quantity DESC \n" + //
            "   LIMIT 5;", nativeQuery = true)
    public List<Object[]> getOrderDetailReport();
}
