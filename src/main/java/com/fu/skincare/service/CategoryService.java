package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.request.category.CreateCategoryRequest;
import com.fu.skincare.response.category.CategoryResponse;

public interface CategoryService {
  public CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest);

  public CategoryResponse getCategoryById(int id);

  public List<CategoryResponse> getAllCategory();
}
