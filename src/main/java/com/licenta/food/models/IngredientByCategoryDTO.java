package com.licenta.food.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class IngredientByCategoryDTO {

    private List<CategoryIngredientsDTO> categoryIngredients;

    public IngredientByCategoryDTO() {
        categoryIngredients = new ArrayList<>();
    }

    public void addCategory(CategoryIngredientsDTO category) {
        categoryIngredients.add(category);
    }
}
