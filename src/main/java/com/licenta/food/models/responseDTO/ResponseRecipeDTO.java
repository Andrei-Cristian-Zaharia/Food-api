package com.licenta.food.models.responseDTO;

import com.licenta.food.models.Person;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResponseRecipeDTO {

    private String name;
    private String description;
    private String howToPrepare;
    private int time;
    private int difficulty;
    private String imageAddress;
    private Integer missingIngredients = 0;
    private Person person;
    private List<IngredientOnRecipeResponseDTO> ingredientList;
}
