package com.coderiders.happyanimal.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "age")
    private int age;

    @Column
    private int height;

    @Column
    private double weight;

    @Column
    private String animalClass;

    @Column
    private String squad;

    @Column
    private String kind;

    @Column
    private String location;

    @Column
    private String status;
}
