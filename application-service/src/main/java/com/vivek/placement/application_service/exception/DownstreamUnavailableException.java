package com.vivek.placement.application_service.exception;

public class DownstreamUnavailableException extends RuntimeException {
    public DownstreamUnavailableException(String service) {
        super(service + " is temporarily unavailable, please try again shortly");
    }
}
