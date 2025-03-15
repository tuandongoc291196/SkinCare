package com.fu.skincare.request.userTestResult;

import java.util.List;

import com.fu.skincare.request.userTest.CreateUserTestRequest;

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
public class CreateUserTestResultRequest {
    private int accountId;
    private List<CreateUserTestRequest> userTests;
}
