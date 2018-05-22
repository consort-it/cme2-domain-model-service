package com.consort.util;

public enum StatusMessage {

    CHECK_HEALTH("Server is Up");

    private String value;

    StatusMessage(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
