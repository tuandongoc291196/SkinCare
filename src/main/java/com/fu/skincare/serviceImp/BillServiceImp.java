package com.fu.skincare.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.account.AccountErrorMessage;
import com.fu.skincare.constants.message.bill.BillErrorMessage;
import com.fu.skincare.constants.message.billHistory.BillHistorySuccessMessage;
import com.fu.skincare.constants.message.product.ProductErrorMessage;
import com.fu.skincare.entity.Account;
import com.fu.skincare.entity.Bill;
import com.fu.skincare.entity.BillHistory;
import com.fu.skincare.entity.OrderDetail;
import com.fu.skincare.exception.EmptyException;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.AccountRepository;
import com.fu.skincare.repository.BillHistoryRepository;
import com.fu.skincare.repository.BillRepository;
import com.fu.skincare.repository.OrderDetailRepository;
import com.fu.skincare.request.bill.CreateBillRequest;
import com.fu.skincare.request.orderDetail.CreateOrderDetailRequest;
import com.fu.skincare.response.account.AccountResponse;
import com.fu.skincare.response.bill.BillByAccountResponse;
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
  private final BillHistoryRepository billHistoryRepository;
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
    totalPrice += 25000;
    Bill bill = Bill.builder()
        .account(account)
        .address(request.getAddress())
        .phoneNumber(request.getPhoneNumber())
        .totalPrice(totalPrice)
        .deliveryFee(25000)
        .status(Status.PENDING)
        .createAt(Utils.formatVNDatetimeNow())
        .build();

    Bill billSaved = billRepository.save(bill);
    List<OrderDetailResponse> listOrderDetailResponse = new ArrayList<>();
    listOrderDetails.forEach(orderDetail -> {
      orderDetail.setBill(billSaved);
      OrderDetail orderDetailSaved = orderDetailRepository.save(orderDetail);
      ProductResponse productResponse = Utils.convertProduct(orderDetailSaved.getProduct());
      OrderDetailResponse orderDetailResponse = modelMapper.map(orderDetailSaved, OrderDetailResponse.class);
      orderDetailResponse.setProductResponse(productResponse);
      listOrderDetailResponse.add(orderDetailResponse);
    });
    BillHistory billHistory = BillHistory.builder()
        .bill(bill)
        .status(Status.PENDING)
        .description(BillHistorySuccessMessage.CREATE)
        .createAt(Utils.formatVNDatetimeNow())
        .build();
    billHistoryRepository.save(billHistory);
    BillResponse billResponse = modelMapper.map(billSaved, BillResponse.class);
    AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
    accountResponse.setRoleName(account.getRole().getName());
    billResponse.setAccount(accountResponse);
    billResponse.setListProducts(listOrderDetailResponse);
    return billResponse;
  }

  @Override
  public BillResponse getById(int id) {
    Bill bill = billRepository.findById(id).orElseThrow(
        () -> new ErrorException(BillErrorMessage.NOT_FOUND));
    return Utils.convertBillResponse(bill);
  }

  @Override
  public List<BillResponse> getAll(int pageNo, int pageSize, String sortBy, boolean isAscending) {

    Page<Bill> pageResults;
    if (isAscending) {
      pageResults = billRepository.findAll(
          PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
    } else {
      pageResults = billRepository.findAll(
          PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
    }

    if (!pageResults.hasContent()) {
      throw new EmptyException(BillErrorMessage.EMPTY);
    }

    List<BillResponse> responses = new ArrayList<>();

    for (Bill bill : pageResults.getContent()) {
      BillResponse billResponse = Utils.convertBillResponse(bill);
      responses.add(billResponse);
    }

    return responses;
  }

  @Override
  public List<BillByAccountResponse> getAllByAccountId(int accountId, int pageNo, int pageSize, String sortBy,
      boolean isAscending) {
    Account account = accountRepository.findById(accountId).orElseThrow(
        () -> new ErrorException(AccountErrorMessage.ACCOUNT_NOT_FOUND));

    Page<Bill> pageResults;
    if (isAscending) {
      pageResults = billRepository.findAllByAccount(account,
          PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
    } else {
      pageResults = billRepository.findAllByAccount(account,
          PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
    }

    if (!pageResults.hasContent()) {
      throw new EmptyException(BillErrorMessage.EMPTY);
    }

    List<BillByAccountResponse> responses = new ArrayList<>();

    for (Bill bill : pageResults.getContent()) {
      BillResponse billResponse = Utils.convertBillResponse(bill);
      BillByAccountResponse billByAccountResponse = modelMapper.map(billResponse, BillByAccountResponse.class);
      responses.add(billByAccountResponse);
    }

    return responses;
  }

  @Override
  public BillResponse cancelBill(int id, String reason) {

    Bill bill = billRepository.findById(id).orElseThrow(
        () -> new ErrorException(BillErrorMessage.NOT_FOUND));

    if (!bill.getStatus().equals(Status.PENDING) && !bill.getStatus().equals(Status.APPROVED)) {
      throw new ErrorException(BillErrorMessage.CANCELED);
    }

    bill.setStatus(Status.CANCELED);

    BillHistory billHistory = BillHistory.builder()
        .bill(bill)
        .status(Status.CANCELED)
        .description(reason)
        .createAt(Utils.formatVNDatetimeNow())
        .build();

    billHistoryRepository.save(billHistory);

    for (OrderDetail orderDetail : bill.getOrderDetails()) {
      if (orderDetail.getStatus().equals(Status.APPROVED)) {
        productService.updateProductQuantity(orderDetail.getProduct().getId(),
            orderDetail.getProduct().getQuantity() + orderDetail.getQuantity());
      }
      orderDetail.setStatus(Status.CANCELED);
      orderDetailRepository.save(orderDetail);
    }
    billRepository.saveAndFlush(bill);
    return Utils.convertBillResponse(bill);

  }

  @Override
  public BillResponse rejectBill(int id, String reason) {
    Bill bill = billRepository.findById(id).orElseThrow(
        () -> new ErrorException(BillErrorMessage.NOT_FOUND));

    if (!bill.getStatus().equals(Status.PENDING)) {
      throw new ErrorException(BillErrorMessage.REJECTED);
    }

    bill.setStatus(Status.REJECTED);

    BillHistory billHistory = BillHistory.builder()
        .bill(bill)
        .status(Status.REJECTED)
        .description(reason)
        .createAt(Utils.formatVNDatetimeNow())
        .build();

    billHistoryRepository.save(billHistory);

    for (OrderDetail orderDetail : bill.getOrderDetails()) {
      orderDetail.setStatus(Status.REJECTED);
      orderDetailRepository.save(orderDetail);
    }
    billRepository.saveAndFlush(bill);
    return Utils.convertBillResponse(bill);
  }

  @Override
  public BillResponse approvedBill(int id) {
    Bill bill = billRepository.findById(id).orElseThrow(
        () -> new ErrorException(BillErrorMessage.NOT_FOUND));
    if (!bill.getStatus().equals(Status.PENDING)) {
      throw new ErrorException(BillErrorMessage.APPROVED);
    }

    for (OrderDetail orderDetail : bill.getOrderDetails()) {
      if (orderDetail.getProduct().getQuantity() < orderDetail.getQuantity()) {
        throw new ErrorException(ProductErrorMessage.NOT_ENOUGH);
      }
    }

    bill.setStatus(Status.APPROVED);

    BillHistory billHistory = BillHistory.builder()
        .bill(bill)
        .status(Status.APPROVED)
        .description(BillHistorySuccessMessage.APPROVED)
        .createAt(Utils.formatVNDatetimeNow())
        .build();

    billHistoryRepository.save(billHistory);

    for (OrderDetail orderDetail : bill.getOrderDetails()) {
      productService.updateProductQuantity(orderDetail.getProduct().getId(),
          orderDetail.getProduct().getQuantity() - orderDetail.getQuantity());
      orderDetail.setStatus(Status.APPROVED);
      orderDetailRepository.save(orderDetail);
    }
    billRepository.saveAndFlush(bill);
    return Utils.convertBillResponse(bill);
  }

  @Override
  public BillResponse doneBill(int id) {
    Bill bill = billRepository.findById(id).orElseThrow(
        () -> new ErrorException(BillErrorMessage.NOT_FOUND));

    if (!bill.getStatus().equals(Status.SUCCESS)) {
      throw new ErrorException(BillErrorMessage.DONE);
    }

    bill.setStatus(Status.DONE);

    BillHistory billHistory = BillHistory.builder()
        .bill(bill)
        .status(Status.DONE)
        .description(BillHistorySuccessMessage.DONE)
        .createAt(Utils.formatVNDatetimeNow())
        .build();

    billHistoryRepository.save(billHistory);

    for (OrderDetail orderDetail : bill.getOrderDetails()) {
      orderDetail.setStatus(Status.DONE);
      orderDetailRepository.save(orderDetail);
    }
    billRepository.saveAndFlush(bill);
    return Utils.convertBillResponse(bill);
  }

}