package com.cali.calibank.user.exception;

public class DuplicatedEmailException extends IllegalArgumentException {

    private static final String MESSAGE = "이미 가입된 이메일입니다.";

    public DuplicatedEmailException() {
        super(MESSAGE);
    }

    public DuplicatedEmailException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
