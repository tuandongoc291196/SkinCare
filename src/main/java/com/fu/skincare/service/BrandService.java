package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.request.brand.CreateBrandRequest;
import com.fu.skincare.request.brand.UpdateBrandRequest;
import com.fu.skincare.response.brand.BrandResponse;

public interface BrandService {
  public BrandResponse createBrand(CreateBrandRequest request);

  public BrandResponse getBrandById(int id);

  public BrandResponse updateBrand(UpdateBrandRequest request);

  public List<BrandResponse> getBrandsByCategory(int categoryId);

  public List<BrandResponse> getAllBrands();
}
