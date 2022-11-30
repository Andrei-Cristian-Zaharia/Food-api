package com.licenta.food.models.createRequestDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateRecipeDTO {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String howToPrepare;

    @NotNull
    private List<String> ingredientNames;

    @NotNull
    private List<Integer> ingredientQuantities;

    @NotNull
    private List<String> ingredientMeasurements;

    @NotNull
    private String ownerName;
}
