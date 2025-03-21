package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.request.product.CreateProductRequest;
import com.fu.skincare.request.product.ProductFilterRequest;
import com.fu.skincare.request.product.UpdateProductRequest;
import com.fu.skincare.response.brand.BrandResponse;
import com.fu.skincare.response.category.CategoryResponse;
import com.fu.skincare.response.product.ListProductResponse;
import com.fu.skincare.response.product.ProductByBrandResponse;
import com.fu.skincare.response.product.ProductByCategoryResponse;
import com.fu.skincare.response.product.ProductResponse;

public interface ProductService {
        public ProductResponse createProduct(CreateProductRequest request);

        public ProductResponse updateProduct(UpdateProductRequest request);

        public ProductResponse getProductById(int id);

        public List<ProductResponse> getAllProduct(int pageNo, int pageSize, String sortBy, boolean isAscending);

        public ListProductResponse<BrandResponse, ProductByBrandResponse> getProductByBrand(int brandId, int pageNo,
                        int pageSize, String sortBy,
                        boolean isAscending);

        public ListProductResponse<CategoryResponse, ProductByCategoryResponse> getProductByCategory(int categoryId,
                        int pageNo,
                        int pageSize, String sortBy,
                        boolean isAscending);

        public List<ProductResponse> filterProduct(ProductFilterRequest request);

        public ProductResponse updateProductQuantity(int productId, int quantity);

        public ProductResponse updateProductStatus(int productId, String status);
}
