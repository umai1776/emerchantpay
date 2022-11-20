package com.transactions.payment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Response {
    private boolean isSuccess;
    private String message;
    private Object data;
}
