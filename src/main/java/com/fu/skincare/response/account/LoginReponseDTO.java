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
public class LoginReponseDTO {
  private String token;
  private String roleName;
  private int accountId;
  private String name;
  private String createAt;
  private String email;
  private String phoneNumber;
  private String status;
}
