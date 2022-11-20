package com.transactions.payment.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Column(name = "isActiveState", columnDefinition = "tinyint not null default 1")
    private Boolean state;
}
