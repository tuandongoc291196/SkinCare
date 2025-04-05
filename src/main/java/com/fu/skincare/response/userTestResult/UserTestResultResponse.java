package com.fu.skincare.response.userTestResult;

import java.util.List;

import com.fu.skincare.response.account.AccountResponse;
import com.fu.skincare.response.product.ProductResponse;
import com.fu.skincare.response.skinType.SkinTypeResponse;
import com.fu.skincare.response.userTest.UserTestResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTestResultResponse {
    private int id;
    private String createdAt;
    private int totalPoint;
    private int maxPoint;
    private int testTime;
    private String status;
    private List<UserTestResponse> userTestResponse;
    private List<ProductResponse> suitableProducts;
    private SkinTypeResponse skinType;
    private AccountResponse user;
}
