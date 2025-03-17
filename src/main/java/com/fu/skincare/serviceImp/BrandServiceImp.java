package com.fu.skincare.serviceImp;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.brand.BrandErrorMessage;
import com.fu.skincare.entity.Brand;
import com.fu.skincare.exception.EmptyException;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.BrandRepository;
import com.fu.skincare.request.brand.CreateBrandRequest;
import com.fu.skincare.request.brand.UpdateBrandRequest;
import com.fu.skincare.response.brand.BrandResponse;
import com.fu.skincare.service.BrandService;
import com.fu.skincare.shared.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandServiceImp implements BrandService {

  private final ModelMapper modelMapper;

  private final BrandRepository brandRepository;

  @Override
  public BrandResponse createBrand(CreateBrandRequest request) {
    Brand brand = Brand.builder()
        .name(request.getName())
        .createdAt(Utils.formatVNDatetimeNow())
        .status(Status.ACTIVATED)
        .build();

    Brand brandSaved = brandRepository.save(brand);
    return modelMapper.map(brandSaved, BrandResponse.class);
  }

  @Override
  public BrandResponse getBrandById(int id) {
    Brand brand = brandRepository.findById(id).orElseThrow(() -> new ErrorException(BrandErrorMessage.NOT_FOUND));
    return modelMapper.map(brand, BrandResponse.class);
  }

  @Override
  public List<BrandResponse> getAllBrands() {

    List<Brand> brands = brandRepository.findByStatus(Status.ACTIVATED);

    if (brands.isEmpty()) {
      throw new EmptyException(BrandErrorMessage.EMPTY);
    }

    return Utils.mapList(brands, BrandResponse.class);
  }

  @Override
  public BrandResponse updateBrand(UpdateBrandRequest request) {

    Brand brand = brandRepository.findById(request.getId()).orElseThrow(
        () -> new ErrorException(BrandErrorMessage.NOT_FOUND));

    brand.setName(request.getName());
    brandRepository.save(brand);
    return modelMapper.map(brand, BrandResponse.class);
  }

}
