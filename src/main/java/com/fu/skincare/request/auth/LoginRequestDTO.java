package com.fu.skincare.request.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.fu.skincare.constants.ValidationSize;
import com.fu.skincare.constants.message.validation.ValidationMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequestDTO {

  @Email(message = ValidationMessage.EMAIL_INVALID_MESSAGE)
  private String email;
  @Size(min = ValidationSize.PASSWORD_MIN, max = ValidationSize.PASSWORD_MAX, message = ValidationMessage.PASSWORD_INVALID_MESSAGE)
  private String password;
}
