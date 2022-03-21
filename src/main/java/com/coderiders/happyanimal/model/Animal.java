package com.coderiders.happyanimal.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    @Column(name = "gender")
    private String gender;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;

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

    @Column(name = "features_of_keeping")
    private String featuresOfKeeping;

    @Column(name = "external_features")
    private String externalFeatures;
}
