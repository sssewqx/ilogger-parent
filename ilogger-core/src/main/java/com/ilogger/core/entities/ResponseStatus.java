package com.ilogger.core.entities;

public enum ResponseStatus {
    OK("OK"),
    WARN("WARN"),
    ERROR("ERROR");

    private final String value;

    ResponseStatus(String value) {
        this.value = value;
    }
}

