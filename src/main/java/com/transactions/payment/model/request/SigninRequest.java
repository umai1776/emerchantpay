package com.transactions.payment.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Setter
@Getter
public class SigninRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
