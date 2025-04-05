package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
  Page<Product> findAllByStatus(String status, Pageable pageable);

  @Query(value = "SELECT distinct p.*\n" + //
      "FROM \n" + //
      "    product p\n" + //
      "JOIN \n" + //
      "    category_brand cb ON p.category_brand_id = cb.id\n" + //
      "JOIN\n" + //
      "    product_skin_type pst ON p.id = pst.product_id\n" + //
      "JOIN\n" + //
      "    product_detail pd ON p.id = pd.product_id\n" + //
      "WHERE \n" + //
      "    (UPPER(p.name) LIKE CONCAT('%', UPPER(COALESCE(NULLIF(:name, ''), '')), '%'))\n" + //
      "    AND (pd.price >= COALESCE(NULLIF(:minPrice, 0), 0))\n" + //
      "    AND (pd.price <= COALESCE(NULLIF(:maxPrice, 0), 2147483647))\n" + //
      "    AND (cb.brand_id = COALESCE(NULLIF(:brandId, 0), cb.brand_id))\n" + //
      "    AND (cb.category_id = COALESCE(NULLIF(:categoryId, 0), cb.category_id))\n" + //
      "    AND (pst.skin_type_id = COALESCE(NULLIF(:skinTypeId, 0), pst.skin_type_id))\n" + //
      "    AND p.status = 'ACTIVATED'\n" +
      "    AND pd.status = 'ACTIVATED'\n" +
      "    order by p.id desc;", nativeQuery = true)
  List<Product> filterProduct(String name, int minPrice, int maxPrice, int brandId, int categoryId, int skinTypeId);

  @Query(value = "SELECT COALESCE(SUM(od.quantity), 0) AS noOfSold \n" + //
      "   FROM db_skin_care.order_detail od\n" + //
      "       join product_detail pd on od.product_detail_id  = pd.id\n" + //
      "   WHERE pd.product_id = :productId\n" + //
      "     And od.status not like 'CANCELED' and od.status not like 'REJECTED'", nativeQuery = true)
  int sumNoOfSoldByProductId(int productId);

}
