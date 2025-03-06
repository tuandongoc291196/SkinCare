package com.fu.skincare.serviceImp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.product.ProductErrorMessage;
import com.fu.skincare.entity.OrderDetail;
import com.fu.skincare.entity.Product;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.OrderDetailRepository;
import com.fu.skincare.repository.ProductRepository;
import com.fu.skincare.request.orderDetail.CreateOrderDetailRequest;
import com.fu.skincare.response.orderDetail.OrderDetailResponse;
import com.fu.skincare.service.OrderDetailService;
import com.fu.skincare.shared.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImp implements OrderDetailService {

  private final OrderDetailRepository orderDetailRepository;
  private final ProductRepository productRepository;
  private final ModelMapper modelMapper;

  @Override
  public OrderDetail createOrderDetail(CreateOrderDetailRequest request) {

    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new ErrorException(ProductErrorMessage.NOT_FOUND));

    OrderDetail orderDetail = OrderDetail.builder()
        .product(product)
        .quantity(request.getQuantity())
        .price(request.getQuantity() * product.getPrice())
        .status(Status.ACTIVATED)
        .createAt(Utils.formatVNDatetimeNow())
        .build();

    return orderDetail;
  }

  @Override
  public OrderDetailResponse getOrderDetailById(int id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getOrderDetailById'");
  }

}
