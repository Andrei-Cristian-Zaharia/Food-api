package com.licenta.food.repositories;

import com.licenta.food.models.SavedRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedRecipesRepository extends JpaRepository<SavedRecipe, Long> {

    boolean existsByRecipeIdAndPersonId(Long recipeId, Long personId);

    @Query(value = "SELECT id_recipe " +
            "FROM saved_recipes sr " +
            "JOIN person p " +
            "using(id_person) " +
            "WHERE p.email_address = ?1", nativeQuery = true)
    List<Long> findRecipeByEmail(String email);
}
