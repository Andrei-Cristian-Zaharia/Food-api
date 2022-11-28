package com.licenta.food.models.createRequestDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateIngredientRequest {

    @NotNull
    private String name;

    @NotNull
    private String category;
}
