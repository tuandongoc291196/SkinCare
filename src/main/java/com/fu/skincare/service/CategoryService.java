package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.request.category.CreateCategoryRequest;
import com.fu.skincare.request.category.UpdateCategoryRequest;
import com.fu.skincare.response.category.CategoryResponse;

public interface CategoryService {
  public CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest);

  public CategoryResponse getCategoryById(int id);

  public CategoryResponse updateCategory(UpdateCategoryRequest request);

  public List<CategoryResponse> getAllCategory();
}
