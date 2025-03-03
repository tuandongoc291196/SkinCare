package com.fu.skincare.exception.handle;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.account.AccountErrorMessage;
import com.fu.skincare.exception.AuthorizedException;
import com.fu.skincare.exception.EmptyException;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.exception.UsernameOrPasswordNotFoundException;
import com.fu.skincare.response.ListResponseDTO;
import com.fu.skincare.response.ResponseDTO;

@RestControllerAdvice
public class ExceptionHandlers extends RuntimeException {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String errorMessage = error.getDefaultMessage();
      errors.put("message", errorMessage);
    });
    return errors;
  }

  @ExceptionHandler(value = { UsernameOrPasswordNotFoundException.class, AuthenticationException.class })
  public ResponseEntity<Object> usernameOrPasswordNotFound(AuthenticationException exception) {
    ResponseDTO<Object> dto = new ResponseDTO<>();
    dto.setMessage(AccountErrorMessage.INVALID_USERNAME_PASSWORD);
    dto.setStatus(Status.FAILURE);
    return ResponseEntity.badRequest().body(dto);
  }

  @ExceptionHandler(value = { ErrorException.class })
  public ResponseEntity<Object> ErrorException(ErrorException exception) {
    ResponseDTO<Object> dto = new ResponseDTO<>();
    dto.setMessage(exception.getMessage());
    dto.setStatus(Status.FAILURE);
    return ResponseEntity.badRequest().body(dto);
  }

  @ExceptionHandler(value = EmptyException.class)
  public ResponseEntity<Object> listEmptyException(EmptyException exception) {
    ListResponseDTO<Object> dto = new ListResponseDTO<>();
    dto.setMessage(exception.getMessage());
    dto.setStatus(Status.FAILURE);
    return ResponseEntity.ok().body(dto);
  }

  @ExceptionHandler(value = AuthorizedException.class)
  public ResponseEntity<Object> authorizedException() {
    ListResponseDTO<Object> dto = new ListResponseDTO<>();
    dto.setMessage("Access Denied");
    dto.setStatus(Status.FAILURE);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dto);
  }

}
