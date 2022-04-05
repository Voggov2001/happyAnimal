package com.coderiders.happyanimal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepeatType {
    EVERY_DAY("Every day"),
    EVERY_WEEK("Every week"),
    EVERY_MONTH("Every month");


    String string;

}
