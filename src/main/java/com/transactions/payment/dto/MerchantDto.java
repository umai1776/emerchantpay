package com.transactions.payment.dto;

import com.transactions.payment.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MerchantDto {
    private Long Id;
    private String name;
    private String description;
    private String email;
    private boolean status;
    private Integer totalTransactionSum;
    private Set<Transaction> transactions = new HashSet<>();
}
