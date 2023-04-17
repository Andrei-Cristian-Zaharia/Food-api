package com.licenta.food.services;

import com.licenta.food.models.Ingredient;
import com.licenta.food.models.Recipe;
import com.licenta.food.models.RecipeIngredient;
import com.licenta.food.models.responseDTO.IngredientOnRecipeResponseDTO;
import com.licenta.food.repositories.RecipeIngredientsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeIngredientsService {

    private final ModelMapper modelMapper;
    private final RecipeIngredientsRepository recipeIngredientRepository;

    public RecipeIngredientsService(ModelMapper modelMapper, RecipeIngredientsRepository recipeIngredientRepository) {
        this.modelMapper = modelMapper;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public void addIngredientToRecipe(Recipe recipe, Ingredient ingredient, String measurement) {

        recipeIngredientRepository.save(new RecipeIngredient(recipe, ingredient, measurement));
    }

    public void deletePreviousRelations(Long recipeId) {
        recipeIngredientRepository.deleteAllByRecipe_Id(recipeId);
    }

    public List<IngredientOnRecipeResponseDTO> getIngredientsForRecipe(Long id) {

        return recipeIngredientRepository.findAllByRecipeId(id)
                .stream()
                .map((RecipeIngredient ri) -> {
                    IngredientOnRecipeResponseDTO ingredient = modelMapper.map(
                            ri.getIngredient(),
                            IngredientOnRecipeResponseDTO.class
                    );

                    ingredient.setMeasurementUnit(ri.getMeasurementUnit());

                    return ingredient;
                })
                .toList();
    }
}
