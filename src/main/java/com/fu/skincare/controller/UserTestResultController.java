package com.fu.skincare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.userTestResult.UserTestResultSuccessMessage;
import com.fu.skincare.request.userTestResult.CreateUserTestResultRequest;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.response.userTestResult.UserTestResultResponse;
import com.fu.skincare.service.UserTestResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/userTest")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserTestResultController {

    private final UserTestResultService userTestResultService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Validated @RequestBody CreateUserTestResultRequest request) {
        ResponseDTO<UserTestResultResponse> responseDTO = new ResponseDTO<UserTestResultResponse>();
        UserTestResultResponse data = userTestResultService.createUserTestResult(request);
        responseDTO.setData(data);
        responseDTO.setMessage(UserTestResultSuccessMessage.CREATE);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/")
    // @PreAuthorize(RolePreAuthorize.IS_AUTHENTICATED)
    public ResponseEntity<?> getById(@RequestParam int id) {
        ResponseDTO<UserTestResultResponse> responseDTO = new ResponseDTO<UserTestResultResponse>();
        UserTestResultResponse data = userTestResultService.getById(id);
        responseDTO.setData(data);
        responseDTO.setMessage(UserTestResultSuccessMessage.GET_BY_ID);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

}
