package com.fu.skincare.serviceImp;

import java.util.Optional;

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
import com.fu.skincare.constants.message.position.PositionErrorMessage;
import com.fu.skincare.constants.message.role.RoleErrorMessage;
import com.fu.skincare.entity.Account;
import com.fu.skincare.entity.Position;
import com.fu.skincare.entity.Role;
import com.fu.skincare.entity.Staff;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.jwt.JwtConfig;
import com.fu.skincare.repository.AccountRepository;
import com.fu.skincare.repository.PositionRepository;
import com.fu.skincare.repository.RoleRepository;
import com.fu.skincare.repository.StaffRepository;
import com.fu.skincare.request.auth.LoginRequestDTO;
import com.fu.skincare.request.auth.RegisterCustomerDTO;
import com.fu.skincare.request.auth.RegisterStaffDTO;
import com.fu.skincare.response.account.LoginReponseDTO;
import com.fu.skincare.response.account.RegisterResponse;
import com.fu.skincare.response.account.RegisterStaffResponseDTO;
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
  private final PositionRepository positionRepository;
  private final StaffRepository staffRepository;

  @Override
  public LoginReponseDTO login(LoginRequestDTO loginRequestDTO) {
    Account account = accountRepository.findAccountByEmail(loginRequestDTO.getEmail())
        .orElseThrow(() -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_REGISTER));
    if (!account.getStatus().equals(Status.ACTIVATED)) {
      throw new ErrorException(AccountErrorMessage.ACCOUNT_NOT_ACTIVE);
    }
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

    Optional<Account> checkAccountExist = accountRepository.findAccountByEmail(registerCustomerDTO.getEmail());

    if (checkAccountExist.isPresent()) {
      throw new ErrorException(AccountErrorMessage.EXIST_EMAIL_ACCOUNT);
    }

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
  public RegisterStaffResponseDTO registerStaff(RegisterStaffDTO registerStaffDTO) {
    Role role = roleRepository.findByName(RoleName.ROLE_STAFF)
        .orElseThrow(() -> new ErrorException(RoleErrorMessage.ROLE_NOT_EXIST));

    Optional<Account> checkAccountExist = accountRepository.findAccountByEmail(registerStaffDTO.getEmail());
    Optional<Position> position = positionRepository.findById(registerStaffDTO.getPositionId());

    if (!position.isPresent()) {
      throw new ErrorException(PositionErrorMessage.POSITION_NOT_EXIST);
    }

    if (checkAccountExist.isPresent()) {
      throw new ErrorException(AccountErrorMessage.EXIST_EMAIL_ACCOUNT);
    }

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

    Staff staff = Staff.builder()
        .account(accountSaved)
        .createdAt(Utils.formatVNDatetimeNow())
        .name(registerStaffDTO.getName())
        .position(position.get())
        .status(Status.ACTIVATED)
        .build();

    staffRepository.save(staff);

    RegisterStaffResponseDTO registerResponse = modelMapper.map(accountSaved, RegisterStaffResponseDTO.class);
    registerResponse.setAccountId(accountSaved.getId());
    registerResponse.setRoleName(accountSaved.getRole().getName());
    registerResponse.setPositionName(position.get().getName());
    return registerResponse;
  }

}
