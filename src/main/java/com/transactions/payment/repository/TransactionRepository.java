package com.transactions.payment.repository;

import com.transactions.payment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByUuidAndUser_Id(String uuid, Long userId);
    Optional<Transaction> findByUuidAndMerchant_Id(String uuid, Long merchantId);
    List<Transaction> findAllByUpdatedDateIsBeforeAndStatusIn(Instant time, List<String> status);
}
