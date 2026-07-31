package com.store.exception;

public class StoreBusinessException extends RuntimeException {
private static final long serialVersionUID = 1L;

public StoreBusinessException(String message) {
    super(message);
}
}