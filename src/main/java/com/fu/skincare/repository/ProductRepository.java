package com.fu.skincare.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fu.skincare.entity.Brand;
import com.fu.skincare.entity.Category;
import com.fu.skincare.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
  Page<Product> findAllByStatus(String status, Pageable pageable);

  // Page<Product> findAllByBrandAndStatus(Brand brand, String status, Pageable pageable);

  // Page<Product> findAllByCategoryAndStatus(Category category, String status, Pageable pageable);

  @Query(value = "SELECT distinct p.* FROM db_skin_care.product p  \n" + //
      "   join category c on p.category_id = c.id \n" + //
      "   join brand b on p.brand_id = b.id \n" + //
      "where (:category_id = 0 OR p.category_id = :category_id)  \n" + //
      "    AND (:brand_id = 0 OR p.brand_id = :brand_id)  \n" + //
      "    AND (:maxPrice = 0 OR price <= :maxPrice)  \n" + //
      "    AND (:minPrice = 0 OR price >= :minPrice)" +
      "    AND p.status = 'ACTIVATED'" +
      "    AND (:name = '' OR p.name like CONCAT('%', :name, '%'))", nativeQuery = true)
  List<Product> filterProduct(int category_id, int brand_id, int maxPrice, int minPrice, String name);

  // Page<Product> findAllByCategoryAndStatus(Category category, String activated, PageRequest of);
}
