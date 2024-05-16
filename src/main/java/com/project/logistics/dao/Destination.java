package com.project.logistics.dao;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "destinations")
@Getter
@EqualsAndHashCode
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer distance;

    @OneToMany(cascade = ALL, mappedBy = "destination")
    List<Order> orderList;
}
