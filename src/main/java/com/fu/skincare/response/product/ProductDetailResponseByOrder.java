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
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductDetailResponseByOrder {
    private int id;
    private String name;
    private String description;
    private String ingredient;
    private String effect;
    private String instructionManual;
    private String productSpecifications;
    private String image;
    private int price;
    private String status;
    private String capacity;
    private String createdAt;
    private CategoryResponse category;
    private BrandResponse brand;
    private String createdBy;
}
