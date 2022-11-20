package com.transactions.payment.services;

import com.transactions.payment.exception.ResourceNotFoundException;
import com.transactions.payment.model.Subscription;
import com.transactions.payment.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class SubscribeServices {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Transactional
    public void subscribe(String email) {
        Subscription sub = new Subscription();
        sub.setEmail(email);
        sub.setCreatedDate(LocalDateTime.now());
        sub.setState(true);
        subscriptionRepository.save(sub);
    }

    @Transactional
    public void unsubscribe(Long id) {
        Subscription subscription =
                subscriptionRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Subscription not found with id:  " + id));

        subscription.setState(false);
        subscription.setUpdatedDate(LocalDateTime.now());
        subscriptionRepository.save(subscription);
    }
}


