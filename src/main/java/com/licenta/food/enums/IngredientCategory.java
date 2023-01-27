package com.licenta.food.enums;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

public enum IngredientCategory {

    LEGUME("legume"),
    FRUCTE("fructe"),
    LACTATE("lactate"),
    CARNE("carne"),
    SEMINTE("seminte"),
    INDULCTORI("îndulcitori"),
    CEREALE("cereale"),
    PESTE("pește"),
    ULEIURI_SI_GRASIMI("uleiuri si grăsimi"),
    CONDIMENTE("condimente"),
    PLANTE_SI_VERDEATA("plante si verdeață"),
    SOSURI("sosuri"),
    PASTE("paste");

    private final String displayName;

    IngredientCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static List<String> getCategories() {
        return Arrays.stream(values()).map(IngredientCategory::getDisplayName).toList();
    }
}
