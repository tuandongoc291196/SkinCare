package com.fu.skincare.request.product;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateProductDetailRequest {
    @Min(value = 0, message = "Price must be greater than 0")
    private int price;
    @Min(value = 0, message = "Quantity must be greater than 0")
    private int quantity;
    private String capacity;

}
