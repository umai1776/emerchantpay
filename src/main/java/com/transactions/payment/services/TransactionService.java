package com.transactions.payment.services;

import com.transactions.payment.dto.AuthorizeTransactionRequest;
import com.transactions.payment.dto.Response;
import com.transactions.payment.model.*;
import com.transactions.payment.repository.MerchantRepository;
import com.transactions.payment.repository.TransactionRepository;
import com.transactions.payment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final MerchantRepository merchantRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Response> authorizeTransaction(AuthorizeTransactionRequest request) {
        ResponseEntity<Response> response = null;
        try {
            Optional<User> user = getCurrentUser();
            Optional<Merchant> merchant = merchantRepository.findById(request.getMerchantId());

            if (user.isPresent() && merchant.isPresent()) {
                Transaction transaction = new Transaction();
                transaction.setAmount(request.getAmount().intValue());
                transaction.setStatus(TransactionStatus.CREATED.name());
                transaction.setUuid(UUID.randomUUID().toString());
                transaction.setCustomer_email(user.get().getEmail());
                transaction.setTType(TType.AuthorizedTransaction);
                transaction.setMerchant(merchant.get());
                transaction.setUser(user.get());
                transaction = transactionRepository.save(transaction);
                Map<String, String> data = new HashMap<>();
                data.put("transactionId", transaction.getUuid());
                response = new ResponseEntity<>(Response.builder()
                        .isSuccess(true)
                        .message("Transaction Created")
                        .data(data)
                        .build(), HttpStatus.CREATED);
            } else {
                response = new ResponseEntity<>(Response.builder()
                        .isSuccess(false)
                        .message("Invalid/Inactive Merchant selected")
                        .build(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Exception in authorizeTransaction ", e);
            response = new ResponseEntity<>(Response.builder()
                    .isSuccess(false)
                    .message("Internal Server Error")
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public ResponseEntity<Response> approveTransaction(String transactionId) {
        ResponseEntity<Response> response = null;
        try {
            Optional<User> user = getCurrentUser();

            if (user.isPresent()) {
                Optional<Transaction> transaction = transactionRepository.findByUuidAndUser_Id(transactionId, user.get().getId());
                if (transaction.isPresent() && transaction.get().getStatus().equals(TransactionStatus.CREATED.toString())) {
                    transaction.get().setStatus(TransactionStatus.APPROVED.name());
                    Merchant merchant = transaction.get().getMerchant();
                    merchant.setTotalTransactionSum(merchant.getTotalTransactionSum() + transaction.get().getAmount());
                    transactionRepository.save(transaction.get());
                    merchantRepository.save(merchant);
                    response = new ResponseEntity<>(Response.builder()
                            .isSuccess(true)
                            .message("Transaction Approved")
                            .build(), HttpStatus.OK);
                } else {
                    response = new ResponseEntity<>(Response.builder()
                            .isSuccess(false)
                            .message("Invalid Transaction Selected")
                            .build(), HttpStatus.BAD_REQUEST);
                }
            } else {
                response = new ResponseEntity<>(Response.builder()
                        .isSuccess(false)
                        .message("Invalid Transaction Selected")
                        .build(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Exception in approveTransaction ", e);
            response = new ResponseEntity<>(Response.builder()
                    .isSuccess(false)
                    .message("Internal Server Error")
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public ResponseEntity<Response> getTransactionStatus(String transactionId) {
        ResponseEntity<Response> response = null;
        try {
            Optional<User> user = getCurrentUser();
            if (user.isPresent()) {
                Optional<Transaction> transaction = transactionRepository.findByUuidAndUser_Id(transactionId, user.get().getId());
                if (transaction.isPresent()) {
                    Map<String, String> status = new HashMap<>();
                    status.put("status", transaction.get().getStatus());
                    response = new ResponseEntity<>(Response.builder()
                            .isSuccess(true)
                            .message("Success")
                            .data(status)
                            .build(), HttpStatus.OK);
                } else {
                    response = new ResponseEntity<>(Response.builder()
                            .isSuccess(false)
                            .message("Invalid Transaction Selected")
                            .build(), HttpStatus.BAD_REQUEST);
                }
            } else {
                response = new ResponseEntity<>(Response.builder()
                        .isSuccess(false)
                        .message("Invalid Transaction Selected")
                        .build(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Exception in getTransactionStatus ", e);
            response = new ResponseEntity<>(Response.builder()
                    .isSuccess(false)
                    .message("Internal Server Error")
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public ResponseEntity<Response> deleteTransaction(String transactionId) {
        ResponseEntity<Response> response = null;
        try {
            Optional<User> user = getCurrentUser();
            if (user.isPresent()) {
                Optional<Transaction> transaction = transactionRepository.findByUuidAndUser_Id(transactionId, user.get().getId());
                if (transaction.isPresent()) {
                    transactionRepository.delete(transaction.get());
                    response = new ResponseEntity<>(Response.builder()
                            .isSuccess(true)
                            .message("Transaction Deleted")
                            .build(), HttpStatus.OK);
                } else {
                    response = new ResponseEntity<>(Response.builder()
                            .isSuccess(false)
                            .message("Invalid Transaction Selected")
                            .build(), HttpStatus.BAD_REQUEST);
                }
            } else {
                response = new ResponseEntity<>(Response.builder()
                        .isSuccess(false)
                        .message("Invalid Transaction Selected")
                        .build(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Exception in deleteTransaction ", e);
            response = new ResponseEntity<>(Response.builder()
                    .isSuccess(false)
                    .message("Internal Server Error")
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public void deleteTransactionsOlderThenOneHour(){
        Instant instant = Instant.now();
        Instant olderThenOneHour = instant.minusSeconds(3600);
        List<String> statusList = new LinkedList<>();
        statusList.add(TransactionStatus.CREATED.toString());
        statusList.add(TransactionStatus.CANCELED.toString());
        statusList.add(TransactionStatus.REVERSAL_REQUESTED.toString());

        List<Transaction> list = transactionRepository.findAllByUpdatedDateIsBeforeAndStatusIn(olderThenOneHour, statusList);

        list.stream().forEach(c -> {
            if (c.getUpdatedDate().isBefore(olderThenOneHour)){
                System.out.println("Found Old Transactions");
                transactionRepository.delete(c);
            }
        });
    }
    public ResponseEntity<Response> refundTransactionRequest(String transactionId) {
        ResponseEntity<Response> response = null;
        try {
            Optional<User> user = getCurrentUser();
            if (user.isPresent()) {
                Optional<Transaction> refundableTransaction = transactionRepository.findByUuidAndMerchant_Id(transactionId, user.get().getId());
                if (refundableTransaction.isPresent()) {
                    Transaction transaction = new Transaction();
                    transaction.setUuid(UUID.randomUUID().toString());
                    transaction.setAmount(refundableTransaction.get().getAmount());
                    transaction.setUser(refundableTransaction.get().getUser());
                    transaction.setTType(TType.ReferencedTransaction);
                    transaction.setMerchant(refundableTransaction.get().getMerchant());
                    transaction.setCustomer_email(refundableTransaction.get().getCustomer_email());
                    transaction.setStatus(TransactionStatus.REVERSAL_REQUESTED.name());
                    transaction.setReference_id(refundableTransaction.get().getUuid());
                    transaction = transactionRepository.save(transaction);
                    Map<String, String> data = new HashMap<>();
                    data.put("transactionId", transaction.getUuid());
                    response = new ResponseEntity<>(Response.builder()
                            .isSuccess(true)
                            .message("Refund Transaction Created")
                            .data(data)
                            .build(), HttpStatus.CREATED);
                } else {
                    response = new ResponseEntity<>(Response.builder()
                            .isSuccess(false)
                            .message("Invalid Transaction Selected")
                            .build(), HttpStatus.BAD_REQUEST);
                }
            } else {
                response = new ResponseEntity<>(Response.builder()
                        .isSuccess(false)
                        .message("Invalid Transaction Selected")
                        .build(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Exception in refundTransactionRequest ", e);
            response = new ResponseEntity<>(Response.builder()
                    .isSuccess(false)
                    .message("Internal Server Error")
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public ResponseEntity<Response> approveRefundRequest(String transactionId) {
        ResponseEntity<Response> response = null;
        try {
            Optional<User> user = getCurrentUser();
            if (user.isPresent()) {
                Optional<Transaction> transaction = transactionRepository.findByUuidAndMerchant_Id(transactionId, user.get().getId());
                if (transaction.isPresent()) {
                    Merchant merchant = transaction.get().getMerchant();
                    merchant.setTotalTransactionSum(merchant.getTotalTransactionSum() - transaction.get().getAmount());
                    transaction.get().setTType(TType.RefundedTransaction);
                    transaction.get().setStatus(TransactionStatus.REVERSAL_APPROVED.name());
                    transactionRepository.save(transaction.get());
                    merchantRepository.save(merchant);
                    response = new ResponseEntity<>(Response.builder()
                            .isSuccess(true)
                            .message("Transaction Reversed")
                            .build(), HttpStatus.OK);
                } else {
                    response = new ResponseEntity<>(Response.builder()
                            .isSuccess(false)
                            .message("Invalid Transaction Selected")
                            .build(), HttpStatus.BAD_REQUEST);
                }
            } else {
                response = new ResponseEntity<>(Response.builder()
                        .isSuccess(false)
                        .message("Invalid Transaction Selected")
                        .build(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Exception in refundTransactionRequest ", e);
            response = new ResponseEntity<>(Response.builder()
                    .isSuccess(false)
                    .message("Internal Server Error")
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    private Optional<User> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);
        return user;
    }
}
