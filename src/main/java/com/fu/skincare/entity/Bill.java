package com.fu.skincare.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Bill {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String createAt;
  private String address;
  private String phoneNumber;
  private int totalPrice;
  private String status;

  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "accountId")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Account account;

  @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
  @EqualsAndHashCode.Include
  @ToString.Include
  @JsonIgnore
  private Collection<OrderDetail> orderDetails;

  @OneToMany(mappedBy = "bill", orphanRemoval = true, fetch = FetchType.LAZY)
  @ToString.Exclude
  private Collection<Transaction> transactions;
}
