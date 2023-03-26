package com.licenta.food.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "saved_recipes")
public class SavedRecipe {

    @Id
    @Column(name = "id_person_recipe")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_person")
    private Long personId;

    @Column(name = "id_recipe")
    private Long recipeId;
}
