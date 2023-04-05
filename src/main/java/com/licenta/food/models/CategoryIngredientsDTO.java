package com.licenta.food.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryIngredientsDTO {

    private String categoryName;
    private List<Ingredient> ingredients;

    public CategoryIngredientsDTO() {
        ingredients = new ArrayList<>();
    }
}
