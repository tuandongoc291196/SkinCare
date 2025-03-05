package com.fu.skincare.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  private String description;
  private String image;
  private String price;
  private int quantity;
  private String status;
  private String createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "categoryId")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "brandId")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Brand brand;

  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "staffId")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Staff createdBy;

}
