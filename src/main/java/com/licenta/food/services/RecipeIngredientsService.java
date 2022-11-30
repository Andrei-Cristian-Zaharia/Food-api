package com.licenta.food.services;

import com.licenta.food.models.Ingredient;
import com.licenta.food.models.Recipe;
import com.licenta.food.models.RecipeIngredient;
import com.licenta.food.repositories.RecipeIngredientsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeIngredientsService {

    private final RecipeIngredientsRepository recipeIngredientRepository;

    public RecipeIngredientsService(RecipeIngredientsRepository recipeIngredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public RecipeIngredient addIngredientToRecipe(Recipe recipe, Ingredient ingredient) {

        return recipeIngredientRepository.save(new RecipeIngredient(recipe, ingredient));
    }

    public List<Ingredient> getIngredientsForRecipe(Long id) {

        return recipeIngredientRepository.findAllByRecipeId(id)
                .stream()
                .map(RecipeIngredient::getIngredient)
                .toList();
    }
}
