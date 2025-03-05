package com.fu.skincare.shared;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import com.fu.skincare.entity.Account;
import com.fu.skincare.jwt.JwtConfig;

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
}
