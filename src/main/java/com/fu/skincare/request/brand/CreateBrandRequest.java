package com.fu.skincare.request.brand;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fu.skincare.constants.message.validation.ValidationMessage;
import com.fu.skincare.custom.customAnnotation.ValidPhoneNumber;

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
public class CreateBrandRequest {
  @NotBlank
  private String name;
  @Email(message = ValidationMessage.EMAIL_INVALID_MESSAGE)
  private String email;
  @NotNull(message = ValidationMessage.PHONE_NUMBER_NOT_NULL)
  @ValidPhoneNumber
  private String phoneNumber;
}
