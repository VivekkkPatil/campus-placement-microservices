package com.vivek.placement.application_service.exception;

public class NotEligibleException extends RuntimeException {
    public NotEligibleException(String reason) {
        super("Not eligible: " + reason);
    }
}
