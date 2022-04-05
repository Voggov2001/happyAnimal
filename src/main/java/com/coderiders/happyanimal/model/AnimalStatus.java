package com.coderiders.happyanimal.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "animal_status")
public class AnimalStatus {
    @Id
    private String name;
    @Column(name = "permission_to_participate")
    private boolean permissionToParticipate;
}