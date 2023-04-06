package com.licenta.food.controllers;

import com.licenta.food.services.SavedRecipesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("savedRecipes")
public class SavedRecipesController {

    private final SavedRecipesService savedRecipesService;

    @DeleteMapping("/delete")
    public @ResponseBody ResponseEntity<String> deleteRelation(@RequestParam Long recipeId,
                                                               @RequestParam Long userId) {

        savedRecipesService.deleteRelation(recipeId, userId);
        return ResponseEntity.noContent().build();
    }
}
