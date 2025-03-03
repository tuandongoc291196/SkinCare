package com.fu.skincare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fu.skincare.constants.RolePreAuthorize;
import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.account.AccountSuccessMessage;
import com.fu.skincare.request.auth.LoginRequestDTO;
import com.fu.skincare.request.auth.RegisterCustomerDTO;
import com.fu.skincare.request.auth.RegisterDoctorDTO;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.response.account.LoginReponseDTO;
import com.fu.skincare.response.account.RegisterResponse;
import com.fu.skincare.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

  @Autowired
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@Validated @RequestBody LoginRequestDTO login) {
    ResponseDTO<LoginReponseDTO> responseDTO = new ResponseDTO<LoginReponseDTO>();
    LoginReponseDTO loginResponseDTO = authService.login(login);
    responseDTO.setData(loginResponseDTO);
    responseDTO.setMessage("Login success");
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/register/user")
  public ResponseEntity<?> registerUser(@Validated @RequestBody RegisterCustomerDTO registerDTO) {
    ResponseDTO<RegisterResponse> responseDTO = new ResponseDTO<>();
    RegisterResponse regsiterUserReponse = authService.registerCustomer(registerDTO);
    responseDTO.setData(regsiterUserReponse);
    responseDTO.setMessage(AccountSuccessMessage.CREATE_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/register/doctor")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
  public ResponseEntity<?> registerDoctor(@Validated @RequestBody RegisterDoctorDTO registerDTO) {
    ResponseDTO<RegisterResponse> responseDTO = new ResponseDTO<>();
    RegisterResponse regsiterDoctorReponse = authService.registerDoctor(registerDTO);
    responseDTO.setData(regsiterDoctorReponse);
    responseDTO.setMessage(AccountSuccessMessage.CREATE_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

}
