package com.licenta.food.controllers;

import com.licenta.food.models.responseDTO.ResponseRecipeDTO;
import com.licenta.food.services.SavedRecipesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("savedRecipes")
public class SavedRecipesController {

    private final SavedRecipesService savedRecipesService;

    @DeleteMapping("/delete")
    public @ResponseBody ResponseEntity<String> deleteRelation(@RequestParam Long id) {

        savedRecipesService.deleteRelation(id);
        return ResponseEntity.ok("Deleted relation with id " + id);
    }
}
