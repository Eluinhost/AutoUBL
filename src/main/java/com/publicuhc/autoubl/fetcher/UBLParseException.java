package com.publicuhc.autoubl.fetcher;

public class UBLParseException extends Exception {

    private final String m_message;

    public UBLParseException(String errorMessage) {
        m_message = errorMessage;
    }

    public String getMessage() {
        return m_message;
    }
}
