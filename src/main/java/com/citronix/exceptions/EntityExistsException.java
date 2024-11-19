package com.citronix.exceptions;

public class EntityExistsException extends RuntimeException {
  public EntityExistsException(String message) {
    super(message);
  }
}
