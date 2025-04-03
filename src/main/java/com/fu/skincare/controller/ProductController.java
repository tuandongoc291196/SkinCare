package com.fu.skincare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.skincare.constants.RolePreAuthorize;
import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.product.ProductSuccessMessage;
import com.fu.skincare.request.product.CreateProductRequest;
import com.fu.skincare.request.product.ProductFilterRequest;
import com.fu.skincare.request.product.UpdateProductRequest;
import com.fu.skincare.response.ListResponseDTO;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.response.product.ProductResponse;
import com.fu.skincare.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProductController {

  @Autowired
  private final ProductService productService;

  @PostMapping("/create")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
  public ResponseEntity<?> create(@Validated @RequestBody CreateProductRequest request) {
    ResponseDTO<ProductResponse> responseDTO = new ResponseDTO<ProductResponse>();
    ProductResponse data = productService.createProduct(request);
    responseDTO.setData(data);
    responseDTO.setMessage(ProductSuccessMessage.CREATE_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("/getAll")
  public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0") int pageNo,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "true") boolean isAscending) {
    ListResponseDTO<ProductResponse> responseDTO = new ListResponseDTO<ProductResponse>();
    List<ProductResponse> data = productService.getAllProduct(pageNo, pageSize, sortBy, isAscending);
    responseDTO.setData(data);
    responseDTO.setMessage(ProductSuccessMessage.GET_ALL_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("/filterProduct")
  public ResponseEntity<?> filterProduct(@RequestBody ProductFilterRequest request) {
    ListResponseDTO<ProductResponse> responseDTO = new ListResponseDTO<ProductResponse>();
    List<ProductResponse> data = productService.filterProduct(request);
    responseDTO.setData(data);
    responseDTO.setMessage(ProductSuccessMessage.GET_BY_CATEGORY_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PutMapping("/update")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
  public ResponseEntity<?> updateProduct(@Validated @RequestBody UpdateProductRequest request) {
    ResponseDTO<ProductResponse> responseDTO = new ResponseDTO<ProductResponse>();
    ProductResponse data = productService.updateProduct(request);
    responseDTO.setData(data);
    responseDTO.setMessage(ProductSuccessMessage.UPDATE_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("/")
  public ResponseEntity<?> getProductById(@RequestParam int id) {
    ResponseDTO<ProductResponse> responseDTO = new ResponseDTO<ProductResponse>();
    ProductResponse data = productService.getProductById(id);
    responseDTO.setData(data);
    responseDTO.setMessage(ProductSuccessMessage.GET_BY_ID_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @DeleteMapping("/disable/")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
  public ResponseEntity<?> deleteProduct(@RequestParam int productId) {
    ResponseDTO<ProductResponse> responseDTO = new ResponseDTO<ProductResponse>();
    ProductResponse data = productService.updateProductStatus(productId, Status.DISABLED);
    responseDTO.setData(data);
    responseDTO.setMessage(ProductSuccessMessage.DELETE_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PutMapping("/active/")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
  public ResponseEntity<?> activateProduct(@RequestParam int productId) {
    ResponseDTO<ProductResponse> responseDTO = new ResponseDTO<ProductResponse>();
    ProductResponse data = productService.updateProductStatus(productId, Status.ACTIVATED);
    responseDTO.setData(data);
    responseDTO.setMessage(ProductSuccessMessage.UPDATE_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

}
