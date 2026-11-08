package com.vivek.placement.application_service.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String what, Long id) {
        super(what + " not found with id " + id);
    }
}
