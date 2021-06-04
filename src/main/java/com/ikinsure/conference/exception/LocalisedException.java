package com.ikinsure.conference.exception;

/**
 * Exception with message from resources
 */
public class LocalisedException extends RuntimeException {
    public LocalisedException(String msg) {
        super(msg);
    }
}
