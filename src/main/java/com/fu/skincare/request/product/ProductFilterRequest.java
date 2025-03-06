package com.fu.skincare.request.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterRequest {
  private String name;
  private int brandId;
  private int categoryId;
  private int minPrice;
  private int maxPrice;

}
