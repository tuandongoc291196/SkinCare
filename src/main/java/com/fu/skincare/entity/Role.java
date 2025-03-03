package com.fu.skincare.entity;

import javax.persistence.Entity;

import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Role {
    private int id;
    private String name;
}
