package com.licenta.food.services;

import com.licenta.food.exceptionHandlers.AlreadyExistsException;
import com.licenta.food.models.SavedRecipe;
import com.licenta.food.models.createRequestDTO.AddFavoriteDTO;
import com.licenta.food.repositories.SavedRecipesRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SavedRecipesService {

    private final SavedRecipesRepository savedRecipesRepository;

    public List<Long> getAllRelationsForUserEmail(String email) {

        return savedRecipesRepository.findRecipeByEmail(email);
    }

    public void existItem(AddFavoriteDTO addFavoriteDTO) {
        if (savedRecipesRepository.existsByRecipeIdAndPersonId(
                addFavoriteDTO.getRecipeId(),
                addFavoriteDTO.getUserId())
        ) {
            throw new AlreadyExistsException();
        }
    }

    @Transactional
    public void saveNewRelation(Long userId, Long recipeId) {
        SavedRecipe relation = new SavedRecipe();
        relation.setRecipeId(recipeId);
        relation.setPersonId(userId);

        savedRecipesRepository.save(relation);
    }

    @Transactional
    public void deleteRelation(Long recipeId, Long userId) {
        savedRecipesRepository.deleteByRecipeIdAndPersonId(recipeId, userId);
    }
}
