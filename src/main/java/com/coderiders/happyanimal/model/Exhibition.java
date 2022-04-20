package com.coderiders.happyanimal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "exhibition")
public class Exhibition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @ManyToMany
    private List<Animal> animals;
}
