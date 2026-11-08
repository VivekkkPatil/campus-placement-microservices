package com.vivek.placement.application_service.entity;

public enum ApplicationStatus {
    APPLIED, APTITUDE, TECHNICAL, HR, SELECTED, REJECTED;

    public boolean isFinal() {
        return this == SELECTED || this == REJECTED;
    }

    public ApplicationStatus next() {
        return switch (this) {
            case APPLIED -> APTITUDE;
            case APTITUDE -> TECHNICAL;
            case TECHNICAL -> HR;
            case HR -> SELECTED;
            default -> throw new IllegalStateException("No next round after " + this);
        };
    }
}
