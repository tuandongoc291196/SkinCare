package com.fu.skincare.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.account.AccountErrorMessage;
import com.fu.skincare.constants.message.blog.BlogErrorMessage;
import com.fu.skincare.constants.message.staff.StaffErrorMessage;
import com.fu.skincare.entity.Account;
import com.fu.skincare.entity.Blog;
import com.fu.skincare.entity.Staff;
import com.fu.skincare.exception.EmptyException;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.AccountRepository;
import com.fu.skincare.repository.BlogRepository;
import com.fu.skincare.request.blog.CreateBlogRequest;
import com.fu.skincare.request.blog.UpdateBlogRequest;
import com.fu.skincare.response.blog.BlogResponse;
import com.fu.skincare.service.BlogService;
import com.fu.skincare.shared.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogServiceImp implements BlogService {

    private final BlogRepository blogRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public BlogResponse createBlog(CreateBlogRequest request) {

        Account account = accountRepository.findById(request.getAccountId()).orElseThrow(
                () -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_FOUND));

        Staff staff = account.getStaffs().stream().findFirst()
                .orElseThrow(() -> new ErrorException(StaffErrorMessage.STAFF_NOT_FOUND));
        Blog blog = Blog.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .image(request.getImage())
                .createdBy(staff)
                .createdAt(Utils.formatVNDatetimeNow())
                .status(Status.ACTIVATED)
                .build();

        Blog blogSaved = blogRepository.save(blog);
        BlogResponse response = modelMapper.map(blogSaved, BlogResponse.class);
        response.setAuthorName(staff.getName());
        return response;
    }

    @Override
    public BlogResponse getBlog(int id) {

        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new ErrorException(BlogErrorMessage.BLOG_NOT_FOUND));

        BlogResponse response = modelMapper.map(blog, BlogResponse.class);
        response.setAuthorName(blog.getCreatedBy().getName());
        return response;
    }

    @Override
    public List<BlogResponse> getAllBlog() {
        List<Blog> blogs = blogRepository.findByStatus(Status.ACTIVATED);
        if (blogs.isEmpty()) {
            throw new EmptyException(BlogErrorMessage.EMPTY);
        }

        List<BlogResponse> responses = new ArrayList<>();
        for (Blog blog : blogs) {
            BlogResponse response = modelMapper.map(blog, BlogResponse.class);
            response.setAuthorName(blog.getCreatedBy().getName());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public BlogResponse updateStatus(int id, String status) {

        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new ErrorException(BlogErrorMessage.BLOG_NOT_FOUND));

        blog.setStatus(status);
        Blog blogSaved = blogRepository.save(blog);
        BlogResponse response = modelMapper.map(blogSaved, BlogResponse.class);
        response.setAuthorName(blogSaved.getCreatedBy().getName());
        return response;
    }

    @Override
    public BlogResponse updateBlog(UpdateBlogRequest request) {

        Blog blog = blogRepository.findById(request.getId()).orElseThrow(
                () -> new ErrorException(BlogErrorMessage.BLOG_NOT_FOUND));

        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setImage(request.getImage());
        blogRepository.save(blog);
        BlogResponse response = modelMapper.map(blog, BlogResponse.class);
        response.setAuthorName(blog.getCreatedBy().getName());
        return response;
    }

}
