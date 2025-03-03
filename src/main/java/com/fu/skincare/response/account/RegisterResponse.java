package com.fu.skincare.response.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {
  private int accountId;
  private String roleName;
  private String email;
  private String name;
  private String phoneNumber;
  private String address;
  private String status;
  private String createAt;
}
