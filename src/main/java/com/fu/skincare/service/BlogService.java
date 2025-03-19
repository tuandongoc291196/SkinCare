package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.request.blog.CreateBlogRequest;
import com.fu.skincare.request.blog.UpdateBlogRequest;
import com.fu.skincare.response.blog.BlogResponse;

public interface BlogService {
    public BlogResponse createBlog(CreateBlogRequest request);

    public BlogResponse getBlog(int id);

    public BlogResponse updateBlog(UpdateBlogRequest request);

    public List<BlogResponse> getAllBlog();

    public BlogResponse updateStatus(int id, String status);
}
