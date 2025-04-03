package com.fu.skincare.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  @Column(columnDefinition = "TEXT")
  private String description;
  @Column(columnDefinition = "TEXT")
  private String image;
  @Column(columnDefinition = "TEXT")
  private String ingredient;
  @Column(columnDefinition = "TEXT")
  private String effect;
  @Column(columnDefinition = "TEXT")
  private String instructionManual;
  @Column(columnDefinition = "TEXT")
  private String productSpecifications;
  private int price;
  private int quantity;
  private String status;
  private String createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "staffId")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Staff createdBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "categoryBrandId")
  @EqualsAndHashCode.Include
  @ToString.Include
  private CategoryBrand categoryBrand;
  
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  @EqualsAndHashCode.Include
  @ToString.Include
  @JsonIgnore
  private Collection<OrderDetail> orderDetails;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  @EqualsAndHashCode.Include
  @ToString.Include
  @JsonIgnore
  private Collection<ProductSkinType> productSkinTypes;

}
