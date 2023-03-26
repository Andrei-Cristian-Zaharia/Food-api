package com.licenta.food.repositories;

import com.licenta.food.models.SavedRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedRecipesRepository extends JpaRepository<SavedRecipe, Long> {

    boolean existsByRecipeIdAndPersonId(Long recipeId, Long personId);
}
