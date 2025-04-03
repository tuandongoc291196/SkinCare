package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.request.userTestResult.CreateUserTestResultRequest;
import com.fu.skincare.response.userTestResult.UserTestHistoryResponse;
import com.fu.skincare.response.userTestResult.UserTestResultResponse;

public interface UserTestResultService {
    public UserTestResultResponse createUserTestResult(CreateUserTestResultRequest request);

    public UserTestResultResponse getById(int id);

    public List<UserTestResultResponse> getAll();

    public List<UserTestHistoryResponse> getAllByUser(int accountId);
}
