package com.coderiders.happyanimal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum RepeatType {
    ONCE("Один раз"),
    EVERY_DAY("Каждый день"),
    EVERY_WEEK("Каждую неделю"),
    EVERY_MONTH("Каждый месяц"),
    EVERY_YEAR("Каждый год");

    final String typeName;

    public static List<String> getValues(){
        return Arrays.stream(values())
                .map(repeatType -> repeatType.typeName)
                .collect(Collectors.toList());
    }

    public static RepeatType getByString(String string){
        return Arrays.stream(values())
                .filter(value-> Objects.equals(value.typeName, string))
                .findFirst()
                .orElse(ONCE);
    }
}
