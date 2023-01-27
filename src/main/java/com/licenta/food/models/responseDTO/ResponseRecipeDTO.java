package com.licenta.food.models.responseDTO;

import com.licenta.food.models.Person;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResponseRecipeDTO {

    private Long id;
    private String name;
    private String description;
    private String howToPrepare;
    private int time;
    private int difficulty;
    private int spiciness;
    private boolean isVegan;
    private String imageAddress;
    private Integer missingIngredients = 0;
    private Person person;
    private List<IngredientOnRecipeResponseDTO> ingredientList;
}
