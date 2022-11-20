package com.transactions.payment.repository;

import com.transactions.payment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	User findByEmailIgnoreCase(String email);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
}
