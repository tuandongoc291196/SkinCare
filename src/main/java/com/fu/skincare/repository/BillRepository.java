package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Account;
import com.fu.skincare.entity.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    Page<Bill> findAllByStatus(String status, Pageable pageable);

    Page<Bill> findAllByAccount(Account account, Pageable pageable);

    @Query(value = "SELECT \n" + //
            "    DATE(create_at) AS date, \n" + //
            "    SUM(total_price) AS total_price\n" + //
            "FROM \n" + //
            "    bill\n" + //
            "WHERE \n" + //
            "   status not like 'CANCELED'\n" + //
            "   AND status not like 'REJECTED'\n" + //
            "GROUP BY \n" + //
            "    DATE(create_at);", nativeQuery = true)
    List<Object[]> getBillReport();
}
