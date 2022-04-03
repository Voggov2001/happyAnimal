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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column(name = "permission_to_participate")
    private boolean permissionToParticipate;
}
//    HEALTHY ("Здоров", true),
//    ILL ("Болен", false),
//    DIED ("Мертв", false),
//    ON_INSPECTION ("На осмотре", false),
//    BOOKED_FOR_INSPECTION ("Записан на осмотр", true),
//    IN_HIBERNATION ("В спячке", false),
//    SOLD ("Продан", false),
//    NEWBORN ("Новорожденный", false);