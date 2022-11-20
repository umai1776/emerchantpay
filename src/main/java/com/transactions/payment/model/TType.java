package com.transactions.payment.model;

public enum TType {
    AuthorizedTransaction,
    ChargedTransaction,
    ReversedTransaction,
    RefundedTransaction,
    ApprovedTransaction,
    ReferencedTransaction
}