package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.request.account.EditProfileRequest;
import com.fu.skincare.request.account.FilterAccountRequest;
import com.fu.skincare.response.account.AccountResponse;

public interface AccountService {

    public AccountResponse editProfile(EditProfileRequest request);

    public AccountResponse getById(int id);

    public AccountResponse updateStatus(int id, String status);

    public List<AccountResponse> getAllAccount();

    public List<AccountResponse> filterAccount(FilterAccountRequest request);
}
