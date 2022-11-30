package com.licenta.food.models.responseDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IngredientOnRecipeResponseDTO {

    private Long id;

    private String name;

    private String category;

    private String quantity;
}
