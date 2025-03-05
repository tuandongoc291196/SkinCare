package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.request.brand.CreateBrandRequest;
import com.fu.skincare.response.brand.BrandResponse;

public interface BrandService {
  public BrandResponse createBrand(CreateBrandRequest request);

  public BrandResponse getBrandById(int id);

  public List<BrandResponse> getAllBrands();
}
