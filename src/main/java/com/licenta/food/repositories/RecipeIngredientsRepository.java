package com.licenta.food.repositories;

import com.licenta.food.models.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientsRepository extends JpaRepository<RecipeIngredient, Long> {

    List<RecipeIngredient> findAllByRecipeId(Long id);

    void deleteAllByRecipe_Id(Long recipeId);
}
