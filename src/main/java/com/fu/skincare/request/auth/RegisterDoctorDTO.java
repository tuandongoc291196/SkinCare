package com.fu.skincare.request.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fu.skincare.constants.ValidationSize;
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
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RegisterDoctorDTO {
  @Email(message = ValidationMessage.EMAIL_INVALID_MESSAGE)
  private String email;
  private String name;
  @NotNull(message = ValidationMessage.PHONE_NUMBER_NOT_NULL)
  @ValidPhoneNumber
  private String phoneNumber;
  private String address;
  @Size(min = ValidationSize.PASSWORD_MIN, max = ValidationSize.PASSWORD_MAX, message = ValidationMessage.PASSWORD_INVALID_MESSAGE)
  private String password;
}
