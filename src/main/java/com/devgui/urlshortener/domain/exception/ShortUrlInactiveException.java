package com.devgui.urlshortener.domain.exception;

public class ShortUrlInactiveException extends RuntimeException {
    public ShortUrlInactiveException(String message) {
        super(message);
    }
}
