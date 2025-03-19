package com.fu.skincare.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;
    private long amount;
    private String createAt;
    private String status;
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "bill_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Bill bill;
}
