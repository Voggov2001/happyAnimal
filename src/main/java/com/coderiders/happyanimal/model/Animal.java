package com.coderiders.happyanimal.model;

import com.coderiders.happyanimal.enums.AnimalStatus;
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

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Task> tasks;

    @Column(name = "age")
    private int age;

    @Column
    private int height;

    @Column
    private double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="animal_kind", nullable=false)
    private AnimalKind animalKind;

    @Column
    private String location;

    @Column
    @Enumerated(EnumType.STRING)
    private AnimalStatus status;

    @Column(name = "features_of_keeping")
    private String featuresOfKeeping;

    @Column(name = "external_features")
    private String externalFeatures;
}
