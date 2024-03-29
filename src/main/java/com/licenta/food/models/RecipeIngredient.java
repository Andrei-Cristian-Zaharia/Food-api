package com.licenta.food.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "recipe_ingredient")
@NoArgsConstructor
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recipe_ingredient")
    private Long id;

    @Column(name = "measurement_unit")
    private String measurementUnit;

    @ManyToOne
    @JoinColumn(name = "id_recipe")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "id_ingredient")
    private Ingredient ingredient;

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, String measurementUnit) {
        this.measurementUnit = measurementUnit;
        this.recipe = recipe;
        this.ingredient = ingredient;
    }
}
