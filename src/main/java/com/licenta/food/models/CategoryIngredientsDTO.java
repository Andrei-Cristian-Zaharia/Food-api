package com.licenta.food.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CategoryIngredientsDTO {

    private String categoryName;
    private List<Ingredient> ingredients;
}
