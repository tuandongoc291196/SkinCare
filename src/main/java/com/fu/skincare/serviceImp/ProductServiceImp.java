package com.fu.skincare.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.RoleName;
import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.account.AccountErrorMessage;
import com.fu.skincare.constants.message.brand.BrandErrorMessage;
import com.fu.skincare.constants.message.category.CategoryErrorMessage;
import com.fu.skincare.constants.message.product.ProductErrorMessage;
import com.fu.skincare.constants.message.skinType.SkinTypeErrorMessage;
import com.fu.skincare.constants.message.staff.StaffErrorMessage;
import com.fu.skincare.entity.Account;
import com.fu.skincare.entity.Brand;
import com.fu.skincare.entity.Category;
import com.fu.skincare.entity.CategoryBrand;
import com.fu.skincare.entity.Product;
import com.fu.skincare.entity.ProductSkinType;
import com.fu.skincare.entity.SkinType;
import com.fu.skincare.exception.EmptyException;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.AccountRepository;
import com.fu.skincare.repository.BrandRepository;
import com.fu.skincare.repository.CategoryBrandRepository;
import com.fu.skincare.repository.CategoryRepository;
import com.fu.skincare.repository.ProductRepository;
import com.fu.skincare.repository.ProductSkinTypeRepository;
import com.fu.skincare.repository.SkinTypeRepository;
import com.fu.skincare.request.product.CreateProductRequest;
import com.fu.skincare.request.product.ProductFilterRequest;
import com.fu.skincare.request.product.UpdateProductRequest;
import com.fu.skincare.response.brand.BrandResponse;
import com.fu.skincare.response.category.CategoryResponse;
import com.fu.skincare.response.product.ListProductResponse;
import com.fu.skincare.response.product.ProductByBrandResponse;
import com.fu.skincare.response.product.ProductByCategoryResponse;
import com.fu.skincare.response.product.ProductResponse;
import com.fu.skincare.service.ProductService;
import com.fu.skincare.shared.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

  private final ProductRepository productRepository;
  private final BrandRepository brandRepository;
  private final CategoryRepository categoryRepository;
  private final AccountRepository accountRepository;
  private final ModelMapper modelMapper;
  private final SkinTypeRepository skinTypeRepository;
  private final ProductSkinTypeRepository productSkinTypeRepository;
  private final CategoryBrandRepository categoryBrandRepository;

  @Override
  public ProductResponse createProduct(CreateProductRequest request) {

    Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow(
        () -> new ErrorException(BrandErrorMessage.NOT_FOUND));

    Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
        () -> new ErrorException(CategoryErrorMessage.CATEGORY_NOT_FOUND));

    Account account = accountRepository.findById(request.getAccountId()).orElseThrow(
        () -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_FOUND));

    if (account.getRole().equals(RoleName.ROLE_USER)) {
      throw new ErrorException(AccountErrorMessage.ACCOUNT_NOT_PERMISSION);
    }

    List<SkinType> listSkinType = new ArrayList<SkinType>();

    for (int skinType : request.getSkinTypeId()) {
      SkinType skinTypeEntity = skinTypeRepository.findById(skinType)
          .orElseThrow(() -> new ErrorException(SkinTypeErrorMessage.NOT_FOUND));
      listSkinType.add(skinTypeEntity);
    }
    CategoryBrand categoryBrand = categoryBrandRepository.findByCategoryAndBrand(category, brand);
    if (categoryBrand == null) {
      CategoryBrand categoryBrandEntity = CategoryBrand.builder()
          .category(category)
          .brand(brand)
          .createdAt(Utils.formatVNDatetimeNow())
          .status(Status.ACTIVATED)
          .build();
      categoryBrand = categoryBrandRepository.save(categoryBrandEntity);
    }
    Product product = Product.builder()
        .name(request.getName())
        .description(request.getDescription())
        .image(request.getImage())
        .price(request.getPrice())
        .quantity(request.getQuantity())
        .categoryBrand(categoryBrand)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(Status.ACTIVATED)
        .createdBy(account.getStaffs().stream().findFirst()
            .orElseThrow(() -> new ErrorException(StaffErrorMessage.STAFF_NOT_FOUND)))
        .build();

    Product productSaved = productRepository.save(product);

    List<ProductSkinType> listProductSkinType = new ArrayList<ProductSkinType>();
    if (!listSkinType.isEmpty()) {
      for (SkinType skinType : listSkinType) {
        ProductSkinType productSkinType = ProductSkinType.builder()
            .product(productSaved)
            .skinType(skinType)
            .build();

        ProductSkinType productSkinTypeSaved = productSkinTypeRepository.save(productSkinType);
        listProductSkinType.add(productSkinTypeSaved);
      }
    }
    productSaved.setProductSkinTypes(listProductSkinType);
    ProductResponse response = Utils.convertProduct(productSaved);
    return response;

  }

  @Override
  public ProductResponse getProductById(int id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ErrorException(ProductErrorMessage.NOT_FOUND));
    ProductResponse response = Utils.convertProduct(product);
    return response;
  }

  @Override
  public List<ProductResponse> getAllProduct(int pageNo, int pageSize, String sortBy, boolean isAscending) {

    Page<Product> products;

    if (isAscending) {
      products = productRepository.findAllByStatus(Status.ACTIVATED,
          PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
    } else {
      products = productRepository.findAllByStatus(Status.ACTIVATED,
          PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
    }

    if (products.isEmpty()) {
      throw new EmptyException(ProductErrorMessage.EMPTY);
    }

    List<ProductResponse> response = new ArrayList<ProductResponse>();

    for (Product product : products) {
      ProductResponse productResponse = Utils.convertProduct(product);
      response.add(productResponse);
    }

    return response;
  }

  @Override
  public List<ProductResponse> filterProduct(ProductFilterRequest request) {

    if (request.getMaxPrice() < request.getMinPrice()) {
      throw new EmptyException(ProductErrorMessage.EMPTY);
    }
    if (request.getCategoryId() > 0) {
      categoryRepository.findById(request.getCategoryId())
          .orElseThrow(() -> new EmptyException(ProductErrorMessage.EMPTY));
    }
    if (request.getBrandId() > 0) {
      brandRepository.findById(request.getBrandId())
          .orElseThrow(() -> new EmptyException(ProductErrorMessage.EMPTY));
    }

    List<Product> products = productRepository.filterProduct(request.getCategoryId(), request.getBrandId(),
        request.getMaxPrice(), request.getMinPrice(), request.getName());

    if (products.isEmpty()) {
      throw new EmptyException(ProductErrorMessage.EMPTY);
    }

    List<ProductResponse> response = new ArrayList<ProductResponse>();

    for (Product product : products) {
      ProductResponse productResponse = Utils.convertProduct(product);
      response.add(productResponse);
    }

    return response;
  }

  @Override
  public ListProductResponse<BrandResponse, ProductByBrandResponse> getProductByBrand(int brandId, int pageNo,
      int pageSize, String sortBy, boolean isAscending) {

    Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new ErrorException(BrandErrorMessage.NOT_FOUND));

    Page<Product> products = null;

    // if (isAscending) {
    //   products = productRepository.findAllByBrandAndStatus(brand, Status.ACTIVATED,
    //       PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
    // } else {
    //   products = productRepository.findAllByBrandAndStatus(brand, Status.ACTIVATED,
    //       PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
    // }

    if (products.isEmpty()) {
      throw new EmptyException(ProductErrorMessage.EMPTY);
    }

    ListProductResponse<BrandResponse, ProductByBrandResponse> response = new ListProductResponse<BrandResponse, ProductByBrandResponse>();
    BrandResponse brandResponse = modelMapper.map(brand, BrandResponse.class);
    response.setDetails(brandResponse);
    List<ProductByBrandResponse> listByBrand = new ArrayList<ProductByBrandResponse>();
    for (Product product : products) {
      ProductResponse productResponse = Utils.convertProduct(product);
      ProductByBrandResponse productByBrandResponse = modelMapper.map(productResponse, ProductByBrandResponse.class);
      listByBrand.add(productByBrandResponse);
    }

    response.setProducts(listByBrand);
    return response;
  }

  @Override
  public ListProductResponse<CategoryResponse, ProductByCategoryResponse> getProductByCategory(int categoryId,
      int pageNo, int pageSize, String sortBy, boolean isAscending) {

    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ErrorException(CategoryErrorMessage.CATEGORY_NOT_FOUND));

    Page<Product> products = null;

    // if (isAscending) {
    //   products = productRepository.findAllByCategoryAndStatus(category, Status.ACTIVATED,
    //       PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
    // } else {
    //   products = productRepository.findAllByCategoryAndStatus(category, Status.ACTIVATED,
    //       PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
    // }

    if (products.isEmpty()) {
      throw new EmptyException(ProductErrorMessage.EMPTY);
    }

    ListProductResponse<CategoryResponse, ProductByCategoryResponse> response = new ListProductResponse<CategoryResponse, ProductByCategoryResponse>();
    CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);
    response.setDetails(categoryResponse);
    List<ProductByCategoryResponse> listByCategory = new ArrayList<ProductByCategoryResponse>();
    for (Product product : products) {
      ProductResponse productResponse = Utils.convertProduct(product);
      ProductByCategoryResponse productByCategoryResponse = modelMapper.map(productResponse,
          ProductByCategoryResponse.class);
      listByCategory.add(productByCategoryResponse);
    }

    response.setProducts(listByCategory);
    return response;
  }

  @Override
  public ProductResponse updateProduct(UpdateProductRequest request) {
    Product product = productRepository.findById(request.getId())
        .orElseThrow(() -> new ErrorException(ProductErrorMessage.NOT_FOUND));
    Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> new ErrorException(CategoryErrorMessage.CATEGORY_NOT_FOUND));

    Brand brand = brandRepository.findById(request.getBrandId())
        .orElseThrow(() -> new ErrorException(BrandErrorMessage.NOT_FOUND));

    CategoryBrand categoryBrand = categoryBrandRepository.findByCategoryAndBrand(category, brand);
    if (categoryBrand == null) {
      CategoryBrand categoryBrandEntity = CategoryBrand.builder()
          .category(category)
          .brand(brand)
          .createdAt(Utils.formatVNDatetimeNow())
          .status(Status.ACTIVATED)
          .build();
      categoryBrand = categoryBrandRepository.save(categoryBrandEntity);
    }
    product.setName(request.getName());
    product.setDescription(request.getDescription());
    product.setImage(request.getImage());
    product.setPrice(request.getPrice());
    product.setQuantity(request.getQuantity());
    product.setCategoryBrand(categoryBrand);

    Product productSaved = productRepository.save(product);
    ProductResponse response = Utils.convertProduct(productSaved);
    return response;

  }

  @Override
  public ProductResponse updateProductQuantity(int productId, int quantity) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ErrorException(ProductErrorMessage.NOT_FOUND));
    product.setQuantity(quantity);
    Product productSaved = productRepository.save(product);
    ProductResponse response = Utils.convertProduct(productSaved);
    return response;
  }

  @Override
  public ProductResponse updateProductStatus(int productId, String status) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ErrorException(ProductErrorMessage.NOT_FOUND));

    product.setStatus(status);
    productRepository.save(product);
    return Utils.convertProduct(product);
  }
}
