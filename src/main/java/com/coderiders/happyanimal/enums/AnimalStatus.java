package com.coderiders.happyanimal.enums;


import com.coderiders.happyanimal.exceptions.NotFoundException;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public enum AnimalStatus {
    FINE("Здоров", true),
    SICK("Заболел", false),
    DEAD("Умер", false),
    INSPECTED("На осмотре", false),
    BOOKED_INSPECTION("Записан на осмотр", false),
    BOOKED_EXHIBITION("Записан на выставку", false),
    HIBERNATION("В спячке", false),
    SOLD("Продан", false),
    NEWBORN("Новорожденный", false),
    TREATMENT("На лечении", false);

    private final String name;
    private final boolean permissionToParticipate;

    AnimalStatus(String name, boolean permissionToParticipate) {
        this.name = name;
        this.permissionToParticipate = permissionToParticipate;
    }

    public static AnimalStatus getByName(String name) {
        for (AnimalStatus status : values()) {
            if (Objects.equals(name, status.name)) {
                return status;
            }
        }
        throw new NotFoundException("Статус не найден");
    }

    public static List<String> getAllStatusNames(){
        return Arrays.stream(values())
                .map(v-> v.name)
                .collect(Collectors.toList());
    }
}
