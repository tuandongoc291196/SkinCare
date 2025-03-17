package com.fu.skincare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.skincare.constants.RolePreAuthorize;
import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.category.CategorySuccessMessage;
import com.fu.skincare.request.category.CreateCategoryRequest;
import com.fu.skincare.request.category.UpdateCategoryRequest;
import com.fu.skincare.response.ListResponseDTO;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.response.category.CategoryResponse;
import com.fu.skincare.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  @PostMapping("/create")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
  public ResponseEntity<?> create(@Validated @RequestBody CreateCategoryRequest request) {
    ResponseDTO<CategoryResponse> responseDTO = new ResponseDTO<CategoryResponse>();
    CategoryResponse data = categoryService.createCategory(request);
    responseDTO.setData(data);
    responseDTO.setMessage(CategorySuccessMessage.CATEGORY_CREATED_SUCCESSFULLY);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("/get-all")
  public ResponseEntity<?> getAll() {
    ListResponseDTO<CategoryResponse> responseDTO = new ListResponseDTO<CategoryResponse>();
    List<CategoryResponse> data = categoryService.getAllCategory();
    responseDTO.setData(data);
    responseDTO.setMessage(CategorySuccessMessage.GET_ALL_CATEGORY_SUCCESSFULLY);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("/")
  public ResponseEntity<?> getById(@Validated @RequestParam int id) {
    ResponseDTO<CategoryResponse> responseDTO = new ResponseDTO<CategoryResponse>();
    CategoryResponse data = categoryService.getCategoryById(id);
    responseDTO.setData(data);
    responseDTO.setMessage(CategorySuccessMessage.GET_CATEGORY_BY_ID_SUCCESSFULLY);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PutMapping("/edit/")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
  public ResponseEntity<?> update(@Validated @RequestBody UpdateCategoryRequest request) {
    ResponseDTO<CategoryResponse> responseDTO = new ResponseDTO<CategoryResponse>();
    CategoryResponse data = categoryService.updateCategory(request);
    responseDTO.setData(data);
    responseDTO.setMessage(CategorySuccessMessage.CATEGORY_UPDATED_SUCCESSFULLY);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

}
