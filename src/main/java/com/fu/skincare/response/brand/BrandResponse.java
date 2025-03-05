package com.fu.skincare.response.brand;

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
public class BrandResponse {
  private int id;
  private String name;
  private String email;
  private String phoneNumber;
  private String createdAt;
  private String status;
}
