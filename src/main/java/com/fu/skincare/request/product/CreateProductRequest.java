package com.fu.skincare.request.product;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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
public class CreateProductRequest {
  @NotBlank
  private String name;
  @NotBlank
  private String description;
  private String image;
  @Min(value = 0, message = "Price must be greater than 0")
  private int price;
  @Min(value = 0, message = "Quantity must be greater than 0")
  private int quantity;
  private int categoryId;
  private int brandId;
  private int accountId;
}
