package com.licenta.food.models.responseDTO;

import com.licenta.food.models.Person;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResponseRecipeDTO {

    private String name;
    private String description;
    private String howToPrepare;
    private Person person;
    private List<IngredientOnRecipeResponseDTO> ingredientList;
}
