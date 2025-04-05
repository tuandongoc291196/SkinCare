package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Account;
import com.fu.skincare.entity.Bill;
import com.fu.skincare.response.bill.BillStatusReport;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
        Page<Bill> findAllByStatus(String status, Pageable pageable);

        Page<Bill> findAllByAccount(Account account, Pageable pageable);

        @Query(value = "SELECT DATE(create_at) AS date, SUM(total_price) AS total_price\n" + //
                        "FROM bill\n" + //
                        "WHERE status like 'SUCCESS'\n" + //
                        "\tOR status like 'DONE'\n" + //
                        "GROUP BY DATE(create_at);", nativeQuery = true)
        List<Object[]> getBillReport();

        @Query(value = "SELECT \n" + //
                        "    COALESCE(count(distinct id), 0) AS total,\n" + //
                        "    COALESCE(SUM(CASE WHEN status = 'PENDING' THEN 1 ELSE 0 END), 0) AS pending,\n" + //
                        "    COALESCE(SUM(CASE WHEN status = 'APPROVED' THEN 1 ELSE 0 END), 0) AS approved,\n" + //
                        "    COALESCE(SUM(CASE WHEN status = 'REJECTED' THEN 1 ELSE 0 END), 0) AS rejected,\n" + //
                        "    COALESCE(SUM(CASE WHEN status = 'CANCELED' THEN 1 ELSE 0 END), 0) AS canceled,\n" + //
                        "    COALESCE(SUM(CASE WHEN status = 'SUCCESS' THEN 1 ELSE 0 END), 0) AS success,\n" + //
                        "    COALESCE(SUM(CASE WHEN status = 'DONE' THEN 1 ELSE 0 END), 0) AS done\n" + //
                        "FROM \n" + //
                        "    bill;", nativeQuery = true)
        BillStatusReport getBillStatusReport();
}
