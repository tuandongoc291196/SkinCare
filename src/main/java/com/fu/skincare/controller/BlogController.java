package com.fu.skincare.controller;

import java.util.List;

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
import com.fu.skincare.constants.message.blog.BlogSuccessMessage;
import com.fu.skincare.request.blog.CreateBlogRequest;
import com.fu.skincare.request.blog.UpdateBlogRequest;
import com.fu.skincare.response.ListResponseDTO;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.response.blog.BlogResponse;
import com.fu.skincare.service.BlogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/blog")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping("/create")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> create(@Validated @RequestBody CreateBlogRequest request) {
        ResponseDTO<BlogResponse> responseDTO = new ResponseDTO<BlogResponse>();
        BlogResponse data = blogService.createBlog(request);
        responseDTO.setData(data);
        responseDTO.setMessage(BlogSuccessMessage.CREATE_BLOG_SUCCESS);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("/edit")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> update(@Validated @RequestBody UpdateBlogRequest request) {
        ResponseDTO<BlogResponse> responseDTO = new ResponseDTO<BlogResponse>();
        BlogResponse data = blogService.updateBlog(request);
        responseDTO.setData(data);
        responseDTO.setMessage(BlogSuccessMessage.UPDATE);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/")
    public ResponseEntity<?> getById(@RequestParam int id) {
        ResponseDTO<BlogResponse> responseDTO = new ResponseDTO<BlogResponse>();
        BlogResponse data = blogService.getBlog(id);
        responseDTO.setData(data);
        responseDTO.setMessage(BlogSuccessMessage.GET_BLOG_SUCCESS);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        ListResponseDTO<BlogResponse> responseDTO = new ListResponseDTO<BlogResponse>();
        List<BlogResponse> data = blogService.getAllBlog();
        responseDTO.setData(data);
        responseDTO.setMessage(BlogSuccessMessage.GET_ALL_BLOG_SUCCESS);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/delete/")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> deleteBlog(@RequestParam int id) {
        ResponseDTO<BlogResponse> responseDTO = new ResponseDTO<BlogResponse>();
        BlogResponse data = blogService.updateStatus(id, Status.DISABLED);
        responseDTO.setData(data);
        responseDTO.setMessage(BlogSuccessMessage.DELETE_SUCCESS);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("/activate/")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> activateBlog(@RequestParam int id) {
        ResponseDTO<BlogResponse> responseDTO = new ResponseDTO<BlogResponse>();
        BlogResponse data = blogService.updateStatus(id, Status.ACTIVATED);
        responseDTO.setData(data);
        responseDTO.setMessage(BlogSuccessMessage.ACTIVATE_SUCCESS);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
