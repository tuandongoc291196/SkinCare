package com.fu.skincare.response.product;

import com.fu.skincare.response.brand.BrandResponse;
import com.fu.skincare.response.category.CategoryResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
  private int id;
  private String name;
  private String description;
  private String image;
  private int price;
  private int quantity;
  private String status;
  private String createdAt;
  private CategoryResponse category;
  private BrandResponse brand;
  private String createdBy;
  private String suitableFor;
}
