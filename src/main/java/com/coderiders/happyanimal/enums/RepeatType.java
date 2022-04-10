package com.coderiders.happyanimal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum RepeatType {
    EVERY_DAY("Every day"),
    EVERY_WEEK("Every week"),
    EVERY_MONTH("Every month");


    String string;

    public List<String> getValues(){
        return Arrays.stream(RepeatType.values()).map(value->value.getString()).collect(Collectors.toList());
    }

    public List<RepeatType> getValuesName(){
        return Arrays.stream(RepeatType.values()).collect(Collectors.toList());
    }

}
