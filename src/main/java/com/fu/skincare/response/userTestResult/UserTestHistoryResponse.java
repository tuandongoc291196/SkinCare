package com.fu.skincare.response.userTestResult;

import com.fu.skincare.response.skinType.SkinTypeResponse;

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
public class UserTestHistoryResponse {
  private int id;
  private String createdAt;
  private int totalPoint;
  private int testTime;
  private String status;
  private SkinTypeResponse skinType;
}
