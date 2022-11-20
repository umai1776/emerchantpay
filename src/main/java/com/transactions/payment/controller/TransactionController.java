package com.transactions.payment.controller;

import com.transactions.payment.dto.AuthorizeTransactionRequest;
import com.transactions.payment.dto.Response;
import com.transactions.payment.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PreAuthorize("hasRole('USER') or hasRole('MERCHANT') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Response> authorizeTransaction(@RequestBody AuthorizeTransactionRequest request) {
        return transactionService.authorizeTransaction(request);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MERCHANT') or hasRole('ADMIN')")
    @PostMapping("/approve/{transactionId}")
    public ResponseEntity<Response> approveTransaction(@PathVariable("transactionId") String transactionId) {
        return transactionService.approveTransaction(transactionId);
    }
    @PreAuthorize("hasRole('USER') or hasRole('MERCHANT') or hasRole('ADMIN')")
    @GetMapping("/status")
    public ResponseEntity<Response> getTransactionStatus(@RequestParam("transactionId") String transactionId) {
        return transactionService.getTransactionStatus(transactionId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{transactionId}")
    public ResponseEntity<Response> deleteTransaction(@PathVariable("transactionId") String transactionId) {
        return transactionService.deleteTransaction(transactionId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MERCHANT') or hasRole('ADMIN')")
    @PostMapping("/reverseRequest/{transactionId}")
    public ResponseEntity<Response> requestTransactionReversal(@PathVariable("transactionId") String transactionId) {
        return transactionService.refundTransactionRequest(transactionId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MERCHANT') or hasRole('ADMIN')")
    @PostMapping("/reverseApprove/{transactionId}")
    public ResponseEntity<Response> approveRefundRequest(@PathVariable("transactionId") String transactionId) {
        return transactionService.approveRefundRequest(transactionId);
    }
}
