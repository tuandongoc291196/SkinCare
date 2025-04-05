package com.fu.skincare.serviceImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.fu.skincare.entity.ProductDetail;
import com.fu.skincare.entity.ProductSkinType;
import com.fu.skincare.entity.SkinType;
import com.fu.skincare.exception.EmptyException;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.AccountRepository;
import com.fu.skincare.repository.BrandRepository;
import com.fu.skincare.repository.CategoryBrandRepository;
import com.fu.skincare.repository.CategoryRepository;
import com.fu.skincare.repository.ProductDetailRepostory;
import com.fu.skincare.repository.ProductRepository;
import com.fu.skincare.repository.ProductSkinTypeRepository;
import com.fu.skincare.repository.SkinTypeRepository;
import com.fu.skincare.request.product.CreateProductDetailRequest;
import com.fu.skincare.request.product.CreateProductRequest;
import com.fu.skincare.request.product.ProductFilterRequest;
import com.fu.skincare.request.product.UpdateProductDetailRequest;
import com.fu.skincare.request.product.UpdateProductRequest;
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
  private final ProductDetailRepostory productDetailRepostory;
  // private final ModelMapper modelMapper;
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
        .categoryBrand(categoryBrand)
        .effect(request.getEffect())
        .ingredient(request.getIngredient())
        .instructionManual(request.getInstructionManual())
        .productSpecifications(request.getProductSpecifications())
        .createdAt(Utils.formatVNDatetimeNow())
        .status(Status.ACTIVATED)
        .createdBy(account.getStaffs().stream().findFirst()
            .orElseThrow(() -> new ErrorException(StaffErrorMessage.STAFF_NOT_FOUND)))
        .build();

    Product productSaved = productRepository.save(product);
    List<ProductDetail> listProductDetail = new ArrayList<ProductDetail>();
    for (CreateProductDetailRequest productDetailRequest : request.getProductDetails()) {
      ProductDetail productDetail = ProductDetail.builder()
          .product(productSaved)
          .price(productDetailRequest.getPrice())
          .quantity(productDetailRequest.getQuantity())
          .capacity(productDetailRequest.getCapacity())
          .createdAt(Utils.formatVNDatetimeNow())
          .status(Status.ACTIVATED)
          .build();

      ProductDetail productDetailSaved = productDetailRepostory.save(productDetail);
      listProductDetail.add(productDetailSaved);
    }

    productSaved.setProductDetails(listProductDetail);

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

    List<Product> products = productRepository.filterProduct(request.getName(), request.getMinPrice(),
        request.getMaxPrice(), request.getBrandId(), request.getCategoryId(), request.getSkinTypeId());

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
    product.setCategoryBrand(categoryBrand);
    product.setEffect(request.getEffect());
    product.setIngredient(request.getIngredient());
    product.setInstructionManual(request.getInstructionManual());
    product.setProductSpecifications(request.getProductSpecifications());
    List<ProductDetail> listProductDetail = new ArrayList<ProductDetail>();
    for (UpdateProductDetailRequest productDetailRequest : request.getProductDetails()) {
      Optional<ProductDetail> productDetail = productDetailRepostory.findById(productDetailRequest.getId());

      if (productDetail.isEmpty()) {
        ProductDetail newProductDetail = ProductDetail.builder()
            .product(product)
            .price(productDetailRequest.getPrice())
            .quantity(productDetailRequest.getQuantity())
            .capacity(productDetailRequest.getCapacity())
            .createdAt(Utils.formatVNDatetimeNow())
            .status(Status.ACTIVATED)
            .build();
        ProductDetail productDetailSaved = productDetailRepostory.save(newProductDetail);
        listProductDetail.add(productDetailSaved);
      } else {
        if (productDetail.get().getProduct().getId() != product.getId()) {
          throw new ErrorException(ProductErrorMessage.INVALID_PRODUCT);
        }
        productDetail.get().setPrice(productDetailRequest.getPrice());
        productDetail.get().setQuantity(productDetailRequest.getQuantity());
        productDetail.get().setCapacity(productDetailRequest.getCapacity());
        ProductDetail productDetailSaved = productDetailRepostory.save(productDetail.get());
        listProductDetail.add(productDetailSaved);
      }
    }
    Product productSaved = productRepository.save(product);
    product.setProductDetails(listProductDetail);
    ProductResponse response = Utils.convertProduct(productSaved);
    return response;

  }

  @Override
  public ProductResponse updateProductQuantity(int productDetailId, int quantity) {
    ProductDetail productDetail = productDetailRepostory.findById(productDetailId)
        .orElseThrow(() -> new ErrorException(ProductErrorMessage.NOT_FOUND));
    productDetail.setQuantity(quantity);
    ProductDetail productDetailSaved = productDetailRepostory.save(productDetail);
    ProductResponse response = Utils.convertProduct(productDetailSaved.getProduct());
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
