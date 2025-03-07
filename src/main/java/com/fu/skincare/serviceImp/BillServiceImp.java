package com.fu.skincare.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.account.AccountErrorMessage;
import com.fu.skincare.entity.Account;
import com.fu.skincare.entity.Bill;
import com.fu.skincare.entity.OrderDetail;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.AccountRepository;
import com.fu.skincare.repository.BillRepository;
import com.fu.skincare.repository.OrderDetailRepository;
import com.fu.skincare.request.bill.CreateBillRequest;
import com.fu.skincare.request.orderDetail.CreateOrderDetailRequest;
import com.fu.skincare.response.account.AccountResponse;
import com.fu.skincare.response.bill.BillResponse;
import com.fu.skincare.response.orderDetail.OrderDetailResponse;
import com.fu.skincare.response.product.ProductResponse;
import com.fu.skincare.service.BillService;
import com.fu.skincare.service.OrderDetailService;
import com.fu.skincare.service.ProductService;
import com.fu.skincare.shared.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillServiceImp implements BillService {

  private final BillRepository billRepository;
  private final AccountRepository accountRepository;
  private final OrderDetailRepository orderDetailRepository;
  private final OrderDetailService orderDetailService;
  private final ProductService productService;
  private final ModelMapper modelMapper;

  @Override
  public BillResponse createBill(CreateBillRequest request) {
    Account account = accountRepository.findById(request.getAccountId())
        .orElseThrow(() -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_FOUND));
    List<OrderDetail> listOrderDetails = new ArrayList<>();
    int totalPrice = 0;
    for (CreateOrderDetailRequest createOrderDetailRequest : request.getListProducts()) {
      OrderDetail orderDetail = orderDetailService.createOrderDetail(createOrderDetailRequest);
      listOrderDetails.add(orderDetail);
      totalPrice += orderDetail.getPrice();
    }

    Bill bill = Bill.builder()
        .account(account)
        .address(request.getAddress())
        .phoneNumber(request.getPhoneNumber())
        .totalPrice(totalPrice)
        .status(Status.PENDING)
        .createAt(Utils.formatVNDatetimeNow())
        .build();

    Bill billSaved = billRepository.save(bill);
    List<OrderDetailResponse> listOrderDetailResponse = new ArrayList<>();
    listOrderDetails.forEach(orderDetail -> {
      orderDetail.setBill(billSaved);
      OrderDetail orderDetailSaved = orderDetailRepository.save(orderDetail);
      productService.updateProductQuantity(orderDetailSaved.getProduct().getId(),
          orderDetailSaved.getProduct().getQuantity() - orderDetailSaved.getQuantity());
      ProductResponse productResponse = Utils.convertProduct(orderDetailSaved.getProduct());
      OrderDetailResponse orderDetailResponse = modelMapper.map(orderDetailSaved, OrderDetailResponse.class);
      orderDetailResponse.setProduct(productResponse);
      listOrderDetailResponse.add(orderDetailResponse);
    });

    BillResponse billResponse = modelMapper.map(billSaved, BillResponse.class);
    AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
    billResponse.setAccount(accountResponse);
    billResponse.setListProducts(listOrderDetailResponse);
    return billResponse;
  }

}