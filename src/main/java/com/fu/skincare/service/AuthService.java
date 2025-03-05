package com.fu.skincare.service;

import com.fu.skincare.request.auth.LoginRequestDTO;
import com.fu.skincare.request.auth.RegisterCustomerDTO;
import com.fu.skincare.request.auth.RegisterStaffDTO;
import com.fu.skincare.response.account.LoginReponseDTO;
import com.fu.skincare.response.account.RegisterResponse;
import com.fu.skincare.response.account.RegisterStaffResponseDTO;

public interface AuthService {
  public LoginReponseDTO login(LoginRequestDTO loginRequestDTO);

  public RegisterResponse registerCustomer(RegisterCustomerDTO registerCustomerDTO);

  public RegisterStaffResponseDTO registerStaff(RegisterStaffDTO registerStaffDTO);
}
