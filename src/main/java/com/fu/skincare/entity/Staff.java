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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  private String status;
  private String createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "accountId")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "positionId")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Position position;

  @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
  @EqualsAndHashCode.Include
  @ToString.Include
  @JsonIgnore
  private Collection<Product> products;
}
