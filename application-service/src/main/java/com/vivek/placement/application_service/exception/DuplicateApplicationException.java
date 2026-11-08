package com.vivek.placement.application_service.exception;

public class DuplicateApplicationException extends RuntimeException {
    public DuplicateApplicationException() {
        super("Student has already applied to this drive");
    }
}
