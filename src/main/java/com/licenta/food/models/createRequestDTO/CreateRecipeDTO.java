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
    private int time;

    @NotNull
    private int difficulty;

    @NotNull
    private String imageAddress;

    @NotNull
    private List<String> ingredientNames;

    @NotNull
    private List<Integer> ingredientQuantities;

    @NotNull
    private List<String> ingredientMeasurements;

    @NotNull
    private Integer spiciness;

    @NotNull
    private Boolean isVegan;

    @NotNull
    private String ownerName;
}
