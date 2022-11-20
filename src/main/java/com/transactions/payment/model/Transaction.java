package com.transactions.payment.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String uuid;
    private Integer amount;
    private String status;
    private String customer_email;
    private String customer_phone;
    private Instant createdDate;
    private Instant updatedDate;
    @Column(nullable = true)
    private String reference_id;
    private TType tType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
