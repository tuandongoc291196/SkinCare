package com.fu.skincare.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.brand.BrandErrorMessage;
import com.fu.skincare.constants.message.category.CategoryErrorMessage;
import com.fu.skincare.entity.Brand;
import com.fu.skincare.entity.Category;
import com.fu.skincare.entity.CategoryBrand;
import com.fu.skincare.exception.EmptyException;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.BrandRepository;
import com.fu.skincare.repository.CategoryBrandRepository;
import com.fu.skincare.repository.CategoryRepository;
import com.fu.skincare.request.category.CreateCategoryRequest;
import com.fu.skincare.request.category.UpdateCategoryRequest;
import com.fu.skincare.response.category.CategoryResponse;
import com.fu.skincare.service.CategoryService;
import com.fu.skincare.shared.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

  private final ModelMapper modelMapper;

  private final CategoryRepository categoryRepository;

  private final BrandRepository brandRepository;

  private final CategoryBrandRepository categoryBrandRepository;

  @Override
  public CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {

    Category category = Category.builder()
        .name(createCategoryRequest.getName())
        .description(createCategoryRequest.getDescription())
        .createdAt(Utils.formatVNDatetimeNow())
        .status(Status.ACTIVATED)
        .build();

    Category categorySaved = categoryRepository.save(category);

    return modelMapper.map(categorySaved, CategoryResponse.class);

  }

  @Override
  public CategoryResponse getCategoryById(int id) {
    Category category = categoryRepository.findById(id).orElseThrow(
        () -> new ErrorException(CategoryErrorMessage.CATEGORY_NOT_FOUND));
    return modelMapper.map(category, CategoryResponse.class);
  }

  @Override
  public List<CategoryResponse> getAllCategory() {
    List<Category> categories = categoryRepository.findByStatus(Status.ACTIVATED);
    if (categories.isEmpty()) {
      throw new EmptyException(CategoryErrorMessage.CATEGORY_EMPTY);
    }
    List<CategoryResponse> categoryResponses = Utils.mapList(categories, CategoryResponse.class);
    return categoryResponses;
  }

  @Override
  public CategoryResponse updateCategory(UpdateCategoryRequest request) {
    Category category = categoryRepository.findById(request.getId()).orElseThrow(
        () -> new ErrorException(CategoryErrorMessage.CATEGORY_NOT_FOUND));

    category.setName(request.getName());
    category.setDescription(request.getDescription());
    categoryRepository.save(category);
    return modelMapper.map(category, CategoryResponse.class);

  }

  @Override
  public List<CategoryResponse> getAllByBrand(int brandId) {

    Brand brand = brandRepository.findById(brandId).orElseThrow(
        () -> new ErrorException(BrandErrorMessage.NOT_FOUND));

    List<CategoryBrand> categoryBrands = categoryBrandRepository.findByBrand(brand);
    if (categoryBrands.isEmpty()) {
      throw new EmptyException(CategoryErrorMessage.CATEGORY_EMPTY);
    }
    List<CategoryResponse> response = new ArrayList<>();
    for (CategoryBrand categoryBrand : categoryBrands) {
      Category category = categoryBrand.getCategory();
      CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);
      response.add(categoryResponse);
    }
    return response;
  }
}
