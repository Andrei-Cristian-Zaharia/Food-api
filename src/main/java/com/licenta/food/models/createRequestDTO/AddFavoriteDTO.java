package com.licenta.food.models.createRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddFavoriteDTO {

    private Long recipeId;
    private Long userId;
}
