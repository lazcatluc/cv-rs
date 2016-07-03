package com.zee.cv.exception;

public class GithubApiException extends Exception {

    private static final long serialVersionUID = 1L;

    public GithubApiException() {}

    public GithubApiException(String message) {
        super(message);
    }
}
