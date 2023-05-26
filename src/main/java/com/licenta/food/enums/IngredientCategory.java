package com.licenta.food.enums;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

public enum IngredientCategory {

    vegetable("vegetable"),
    fruits("fruits"),
    dairy("dairy"),
    meat("meat"),
    nuts_and_seeds("nuts and seeds"),
    sweeteners("sweeteners"),
    cereal("cereal"),
    fish("fish"),
    oils_and_fats("oils and fats"),
    spices("spices"),
    plants_and_greens("plants and greens"),
    sauces("sauces"),
    pasta("pasta"),
    drinks("drinks"),
    for_cakes("for cakes"),
    bread("bread"),
    essences("essences");

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
