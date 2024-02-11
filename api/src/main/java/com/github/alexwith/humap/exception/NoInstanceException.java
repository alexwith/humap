package com.github.alexwith.humap.exception;

public class NoInstanceException extends RuntimeException {

    public NoInstanceException() {
        super("No instance was found.");
    }
}
