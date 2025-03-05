package com.fu.skincare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.fu.skincare.constants.message.brand.BrandSuccessMessage;
import com.fu.skincare.request.brand.CreateBrandRequest;
import com.fu.skincare.response.ListResponseDTO;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.response.brand.BrandResponse;
import com.fu.skincare.service.BrandService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/brand")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BrandController {
  @Autowired
  private BrandService brandService;

  @PostMapping("/create")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
  public ResponseEntity<?> create(@Validated @RequestBody CreateBrandRequest request) {
    ResponseDTO<BrandResponse> responseDTO = new ResponseDTO<BrandResponse>();
    BrandResponse data = brandService.createBrand(request);
    responseDTO.setData(data);
    responseDTO.setMessage(BrandSuccessMessage.CREATE_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("/get-all")
  public ResponseEntity<?> getAll() {
    ListResponseDTO<BrandResponse> responseDTO = new ListResponseDTO<BrandResponse>();
    List<BrandResponse> data = brandService.getAllBrands();
    responseDTO.setData(data);
    responseDTO.setMessage(BrandSuccessMessage.GET_ALL_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("/")
  public ResponseEntity<?> getById(@Validated @RequestParam int id) {
    ResponseDTO<BrandResponse> responseDTO = new ResponseDTO<BrandResponse>();
    BrandResponse data = brandService.getBrandById(id);
    responseDTO.setData(data);
    responseDTO.setMessage(BrandSuccessMessage.GET_BY_ID_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

}
