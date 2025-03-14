package com.fu.skincare.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.skincare.constants.RolePreAuthorize;
import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.account.AccountSuccessMessage;
import com.fu.skincare.request.account.EditProfileRequest;
import com.fu.skincare.response.ListResponseDTO;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.response.account.AccountResponse;
import com.fu.skincare.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/account")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/")
    public ResponseEntity<?> getById(@RequestParam int id) {
        ResponseDTO<AccountResponse> responseDTO = new ResponseDTO<>();
        AccountResponse data = accountService.getById(id);
        responseDTO.setData(data);
        responseDTO.setMessage(AccountSuccessMessage.GET_BY_ID);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/getAll")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> getAll() {
        ListResponseDTO<AccountResponse> responseDTO = new ListResponseDTO<>();
        List<AccountResponse> data = accountService.getAllAccount();
        responseDTO.setData(data);
        responseDTO.setMessage(AccountSuccessMessage.GET_ALL_ACCOUNT);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editProfile(@RequestBody EditProfileRequest request) {
        ResponseDTO<AccountResponse> responseDTO = new ResponseDTO<>();
        AccountResponse data = accountService.editProfile(request);
        responseDTO.setData(data);
        responseDTO.setMessage(AccountSuccessMessage.UPDATE_ACCOUNT);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/delete/")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> deleteAccount(@RequestParam int id) {
        ResponseDTO<AccountResponse> responseDTO = new ResponseDTO<>();
        AccountResponse data = accountService.updateStatus(id, Status.DISABLED);
        responseDTO.setData(data);
        responseDTO.setMessage(AccountSuccessMessage.DISABLE_ACCOUNT);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("/activate/")
    public ResponseEntity<?> activateAccount(@RequestParam int id) {
        ResponseDTO<AccountResponse> responseDTO = new ResponseDTO<>();
        AccountResponse data = accountService.updateStatus(id, Status.ACTIVATED);
        responseDTO.setData(data);
        responseDTO.setMessage(AccountSuccessMessage.ACTIVATE_ACCOUNT);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

}
