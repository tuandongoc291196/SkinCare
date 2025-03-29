package com.fu.skincare.shared;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import com.fu.skincare.entity.Account;
import com.fu.skincare.entity.Bill;
import com.fu.skincare.entity.BillHistory;
import com.fu.skincare.entity.Product;
import com.fu.skincare.entity.ProductSkinType;
import com.fu.skincare.jwt.JwtConfig;
import com.fu.skincare.response.account.AccountResponse;
import com.fu.skincare.response.bill.BillResponse;
import com.fu.skincare.response.brand.BrandResponse;
import com.fu.skincare.response.category.CategoryResponse;
import com.fu.skincare.response.orderDetail.OrderDetailResponse;
import com.fu.skincare.response.product.ProductResponse;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class Utils {

  private static final ModelMapper modelMapper = new ModelMapper();

  public static String formatVNDatetimeNow() {
    ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime localDateTime = LocalDateTime.now(vietnamZoneId);
    return localDateTime.format(dateTimeFormatter);
  }

  public static String buildJWT(Authentication authenticate, Account accountAuthenticated, SecretKey secretKey,
      JwtConfig jwtConfig) {
    String token = Jwts.builder().setSubject(authenticate.getName())
        .claim("authorities", authenticate.getAuthorities())
        .claim("email", accountAuthenticated.getEmail())
        .claim("accountId", accountAuthenticated.getId())
        .setIssuedAt((new Date()))
        .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
        .signWith(secretKey).compact();
    return token;
  }

  public static <T, R> List<R> mapList(List<T> inputList, Class<R> outputClass) {
    if (inputList == null) {
      throw new IllegalArgumentException("Input list cannot be null");
    }
    return inputList.stream()
        .map(input -> modelMapper.map(input, outputClass))
        .collect(Collectors.toList());
  }

  public static ProductResponse convertProduct(Product product) {
    ProductResponse response = modelMapper.map(product, ProductResponse.class);
    response.setBrand(modelMapper.map(product.getBrand(), BrandResponse.class));
    response.setCategory(modelMapper.map(product.getCategory(), CategoryResponse.class));
    response.setCreatedBy(product.getCreatedBy().getName());
    String names = "";
    if (!product.getProductSkinTypes().isEmpty()) {
      for (ProductSkinType productSkinType : product.getProductSkinTypes()) {
        names += productSkinType.getSkinType().getType() + ", ";
      }
      response.setSuitableFor(names);
    }
    return response;

  }

  public static BillResponse convertBillResponse(Bill bill) {
    List<OrderDetailResponse> listOrderDetailResponse = new ArrayList<>();
    bill.getOrderDetails().forEach(orderDetail -> {
      ProductResponse productResponse = Utils.convertProduct(orderDetail.getProduct());
      OrderDetailResponse orderDetailResponse = modelMapper.map(orderDetail, OrderDetailResponse.class);
      orderDetailResponse.setProductResponse(productResponse);
      listOrderDetailResponse.add(orderDetailResponse);
    });
    BillResponse billResponse = modelMapper.map(bill, BillResponse.class);
    if (!bill.getBillHistories().isEmpty()) {
      Optional<BillHistory> billHistory = bill.getBillHistories().stream()
          .skip(bill.getBillHistories().size() - 1)
          .findFirst();
      billHistory.ifPresent(history -> billResponse.setReason(history.getDescription()));
    }
    AccountResponse accountResponse = modelMapper.map(bill.getAccount(), AccountResponse.class);
    accountResponse.setRoleName(bill.getAccount().getRole().getName());
    billResponse.setAccount(accountResponse);
    billResponse.setListProducts(listOrderDetailResponse);
    return billResponse;
  }
}
