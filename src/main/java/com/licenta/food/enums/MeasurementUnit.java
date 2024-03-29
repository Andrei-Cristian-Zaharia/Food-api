package com.licenta.food.enums;

import java.util.List;
import java.util.stream.Stream;

public enum MeasurementUnit {

    g, kg, ml, l, linguri, frunze;

    public static Boolean contains(String value) {

        List<String> units = Stream.of(MeasurementUnit.values())
                .map(Enum::name).toList();

        for (String unit : units) {
            if (unit.equals(value)) {
                return true;
            }
        }

        return false;
    }
}
