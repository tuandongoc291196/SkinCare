package com.fu.skincare.exception;

public class UsernameOrPasswordNotFoundException extends RuntimeException {
  public UsernameOrPasswordNotFoundException(String message) {
    super(message);
  }
}