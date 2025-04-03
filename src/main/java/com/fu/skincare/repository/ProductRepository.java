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

  @Query(value = "SELECT p.*\n" + //
      "FROM \n" + //
      "    product p\n" + //
      "JOIN \n" + //
      "    category_brand cb ON p.category_brand_id = cb.id\n" + //
      "JOIN\n" + //
      "    product_skin_type pst ON p.id = pst.product_id\n" + //
      "WHERE \n" + //
      "    (UPPER(p.name) LIKE CONCAT('%', UPPER(COALESCE(NULLIF(:name, ''), '')), '%'))\n" + //
      "    AND (p.price >= COALESCE(NULLIF(:minPrice, 0), 0))\n" + //
      "    AND (p.price <= COALESCE(NULLIF(:maxPrice, 0), 2147483647))\n" + //
      "    AND (cb.brand_id = COALESCE(NULLIF(:brandId, 0), cb.brand_id))\n" + //
      "    AND (cb.category_id = COALESCE(NULLIF(:categoryId, 0), cb.category_id))\n" + //
      "    AND (pst.skin_type_id = COALESCE(NULLIF(:skinTypeId, 0), pst.skin_type_id))\n" + //
      "    AND p.status = 'ACTIVATED' ", nativeQuery = true)
  List<Product> filterProduct(String name, int minPrice, int maxPrice, int brandId, int categoryId, int skinTypeId);

  @Query(value = "SELECT COALESCE(SUM(od.quantity), 0) AS noOfSold\n" + //
      "            FROM product p \n" + //
      "            LEFT JOIN order_detail od ON od.product_id = p.id \n" + //
      "\tJOIN category_brand cb on p.category_brand_id = cb.id \n" + //
      "    where p.id = :productId ", nativeQuery = true)
  int sumNoOfSoldByProductId(int productId);

}
