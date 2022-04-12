//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.coderiders.happyanimal.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inspections")
public class Inspection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date_time")
    private LocalDate date;
    @OneToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @Column(name = "animal_list")
    @JoinTable(name = "animal_inspection",
            joinColumns = {@JoinColumn(name = "inpection_id")},
            inverseJoinColumns = {@JoinColumn(name = "animal_id")})
    private List<Animal> animalList;


}
