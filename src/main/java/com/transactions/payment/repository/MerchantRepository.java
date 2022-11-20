package com.transactions.payment.repository;

import com.transactions.payment.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByName(String username);
}
