package com.fu.skincare.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.skincare.constants.RolePreAuthorize;
import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.skinType.SkinTypeSuccessMessage;
import com.fu.skincare.request.skinType.CreateSkinTypeRequest;
import com.fu.skincare.response.ListResponseDTO;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.response.skinType.SkinTypeResponse;
import com.fu.skincare.service.SkinTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/skinType")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SkinTypeController {
    private final SkinTypeService skinTypeService;

    @PostMapping("/create")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> create(@Validated @RequestBody CreateSkinTypeRequest request) {
        ResponseDTO<SkinTypeResponse> responseDTO = new ResponseDTO<SkinTypeResponse>();
        SkinTypeResponse data = skinTypeService.createSkinType(request);
        responseDTO.setData(data);
        responseDTO.setMessage(SkinTypeSuccessMessage.CREATE_SKIN_TYPE_SUCCESS);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/")
    public ResponseEntity<?> getById(@RequestParam int id) {
        ResponseDTO<SkinTypeResponse> responseDTO = new ResponseDTO<SkinTypeResponse>();
        SkinTypeResponse data = skinTypeService.getSkinType(id);
        responseDTO.setData(data);
        responseDTO.setMessage(SkinTypeSuccessMessage.GET_SKIN_TYPE_SUCCESS);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        ListResponseDTO<SkinTypeResponse> responseDTO = new ListResponseDTO<SkinTypeResponse>();
        List<SkinTypeResponse> data = skinTypeService.getAllSkinTypes();
        responseDTO.setData(data);
        responseDTO.setMessage(SkinTypeSuccessMessage.GET_ALL_SKIN_TYPE_SUCCESS);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

}
