package com.fu.skincare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.skincare.constants.RolePreAuthorize;
import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.position.PositionSuccessMessage;
import com.fu.skincare.request.position.CreatePositionRequest;
import com.fu.skincare.request.position.UpdatePositionRequest;
import com.fu.skincare.response.ListResponseDTO;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.response.position.PositionResponse;
import com.fu.skincare.service.PositionService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/position")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PositionController {
  @Autowired
  private final PositionService positionService;

  @PostMapping("/create")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
  public ResponseEntity<?> create(@Validated @RequestBody CreatePositionRequest request) {
    ResponseDTO<PositionResponse> responseDTO = new ResponseDTO<PositionResponse>();
    PositionResponse positionResponse = positionService.createPostion(request);
    responseDTO.setData(positionResponse);
    responseDTO.setMessage(PositionSuccessMessage.CREATE_POSITION_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("/")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
  public ResponseEntity<?> getPositionById(@Validated @RequestParam int id) {
    ResponseDTO<PositionResponse> responseDTO = new ResponseDTO<PositionResponse>();
    PositionResponse positionResponse = positionService.getPositionById(id);
    responseDTO.setData(positionResponse);
    responseDTO.setMessage(PositionSuccessMessage.GET_POSITION_BY_ID_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("/getAllActivatedPositons")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
  public ResponseEntity<?> getAllActivatesPosition() {
    ListResponseDTO<PositionResponse> responseDTO = new ListResponseDTO<PositionResponse>();
    List<PositionResponse> positionResponse = positionService.getListPosition();
    responseDTO.setData(positionResponse);
    responseDTO.setMessage(PositionSuccessMessage.GET_ALL_POSITION_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("/update/")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
  public ResponseEntity<?> update(UpdatePositionRequest request) {
    ResponseDTO<PositionResponse> responseDTO = new ResponseDTO<PositionResponse>();
    PositionResponse positionResponse = positionService.updatePostion(request);
    responseDTO.setData(positionResponse);
    responseDTO.setMessage(PositionSuccessMessage.UPDATE_POSITION_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

}
