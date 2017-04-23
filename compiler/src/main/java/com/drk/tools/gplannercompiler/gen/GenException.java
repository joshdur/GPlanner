package com.drk.tools.gplannercompiler.gen;

public class GenException extends Exception {

    public GenException(String message) {
        super(message);
    }

    public GenException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
