package com.fu.skincare.serviceImp;

import javax.crypto.SecretKey;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.RoleName;
import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.account.AccountErrorMessage;
import com.fu.skincare.constants.message.role.RoleErrorMessage;
import com.fu.skincare.entity.Account;
import com.fu.skincare.entity.Role;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.jwt.JwtConfig;
import com.fu.skincare.repository.AccountRepository;
import com.fu.skincare.repository.RoleRepository;
import com.fu.skincare.request.auth.LoginRequestDTO;
import com.fu.skincare.request.auth.RegisterCustomerDTO;
import com.fu.skincare.request.auth.RegisterStaffDTO;
import com.fu.skincare.response.account.LoginReponseDTO;
import com.fu.skincare.response.account.RegisterResponse;
import com.fu.skincare.service.AuthService;
import com.fu.skincare.shared.Utils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImp implements AuthService {

  private final AccountRepository accountRepository;
  private final RoleRepository roleRepository;
  private final AuthenticationManager authenticationManager;
  private final SecretKey secretKey;
  private final JwtConfig jwtConfig;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;

  @Override
  public LoginReponseDTO login(LoginRequestDTO loginRequestDTO) {
    Account account = accountRepository.findAccountByEmail(loginRequestDTO.getEmail())
        .orElseThrow(() -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_REGISTER));
    Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),
        loginRequestDTO.getPassword());
    LoginReponseDTO loginResponseDTO = null;
    Authentication authenticate = authenticationManager.authenticate(authentication);
    if (authenticate.isAuthenticated()) {
      String token = Utils.buildJWT(authenticate, account, secretKey, jwtConfig);
      loginResponseDTO = modelMapper.map(account, LoginReponseDTO.class);
      loginResponseDTO.setToken(token);
      loginResponseDTO.setRoleName(account.getRole().getName());
      loginResponseDTO.setAccountId(account.getId());
    }
    return loginResponseDTO;
  }

  @Override
  public RegisterResponse registerCustomer(RegisterCustomerDTO registerCustomerDTO) {
    Role role = roleRepository.findByName(RoleName.ROLE_USER)
        .orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

    Account account = Account.builder()
        .address(registerCustomerDTO.getAddress())
        .name(registerCustomerDTO.getName())
        .email(registerCustomerDTO.getEmail())
        .phoneNumber(registerCustomerDTO.getPhoneNumber())
        .password(passwordEncoder.encode(registerCustomerDTO.getPassword()))
        .createAt(Utils.formatVNDatetimeNow())
        .status(Status.ACTIVATED)
        .role(role)
        .build();
    Account accountSaved = accountRepository.save(account);

    RegisterResponse registerResponse = modelMapper.map(accountSaved, RegisterResponse.class);
    registerResponse.setAccountId(accountSaved.getId());
    registerResponse.setRoleName(accountSaved.getRole().getName());
    return registerResponse;
  }

  @Override
  public RegisterResponse registerStaff(RegisterStaffDTO registerStaffDTO) {
    Role role = roleRepository.findByName(RoleName.ROLE_STAFF)
        .orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

    Account account = Account.builder()
        .address(registerStaffDTO.getAddress())
        .email(registerStaffDTO.getEmail())
        .name(registerStaffDTO.getName())
        .phoneNumber(registerStaffDTO.getPhoneNumber())
        .password(passwordEncoder.encode(registerStaffDTO.getPassword()))
        .createAt(Utils.formatVNDatetimeNow())
        .status(Status.ACTIVATED)
        .role(role)
        .build();
    Account accountSaved = accountRepository.save(account);

    RegisterResponse registerResponse = modelMapper.map(accountSaved, RegisterResponse.class);
    registerResponse.setAccountId(accountSaved.getId());
    registerResponse.setRoleName(accountSaved.getRole().getName());
    return registerResponse;
  }

}
