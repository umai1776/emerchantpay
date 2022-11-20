package com.transactions.payment.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsernameNotFoundexceptionHandler extends UsernameNotFoundException {
    public UsernameNotFoundexceptionHandler(String msg) {
        super(msg);
    }
}
