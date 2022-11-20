package com.transactions.payment.cronjobs;

import com.transactions.payment.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduledTransaction {

    private final TransactionService transactionService;

    @Scheduled(fixedDelay = 1000)
    public void deleteTransactions() {
        transactionService.deleteTransactionsOlderThenOneHour();
    }
}
