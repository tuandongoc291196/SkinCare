package com.fu.skincare.request.product;

import java.util.List;

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
public class UpdateProductRequest {
  private int id;
  private String name;
  private String description;
  private String ingredient;
  private String effect;
  private String instructionManual;
  private String productSpecifications;
  private String image;
  private int categoryId;
  private int brandId;
  private List<Integer> skinTypeIds;
  private List<UpdateProductDetailRequest> productDetails;
}
