package com.licenta.food.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FilterRecipeDTO {

    String recipeName;
    String authorName;
    Integer prepareTimeMin;
    Integer prepareTimeMax;
    Integer rating;
    Integer difficulty;
    Integer spiciness;
    List<String> ingredientsNames;
}
