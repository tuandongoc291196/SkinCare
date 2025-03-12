package com.fu.skincare.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "TEXT")
    private String question;
    private String createdAt;
    private String status;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Answer> answers;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<UserTest> userTests;

    
}
