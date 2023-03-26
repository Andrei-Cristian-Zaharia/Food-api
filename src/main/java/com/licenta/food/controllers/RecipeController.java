package com.licenta.food.controllers;

import com.licenta.food.models.FilterRecipeDTO;
import com.licenta.food.models.Recipe;
import com.licenta.food.models.createRequestDTO.AddFavoriteDTO;
import com.licenta.food.models.createRequestDTO.CreateRecipeDTO;
import com.licenta.food.models.responseDTO.ResponseRecipeDTO;
import com.licenta.food.services.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public @ResponseBody ResponseEntity<Recipe> createNewRecipe(@RequestBody CreateRecipeDTO createRecipeDTO) {
        Recipe recipe = recipeService.createRecipe(createRecipeDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(recipe);
    }

    @GetMapping("/findById")
    public @ResponseBody ResponseEntity<Recipe> getRecipeById(@RequestParam Long id) {
        Recipe result = recipeService.getRecipeById(id);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/info/id/{id}")
    public @ResponseBody ResponseEntity<ResponseRecipeDTO> getRecipeInfoById(@PathVariable Long id) {
        ResponseRecipeDTO result = recipeService.getRecipeInfoById(id);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/findByName")
    public @ResponseBody ResponseEntity<ResponseRecipeDTO> getRecipeByName(@RequestParam String name) {
        ResponseRecipeDTO result = recipeService.getRecipeByName(name);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/all")
    public @ResponseBody ResponseEntity<List<ResponseRecipeDTO>> getAllRecipes() {
        List<ResponseRecipeDTO> recipes = recipeService.getAllRecipes();

        return ResponseEntity.ok().body(recipes);
    }

    @GetMapping("/all/owner/username")
    public @ResponseBody ResponseEntity<List<ResponseRecipeDTO>> getAllRecipesByOwnerUsername(@RequestParam String name) {
        List<ResponseRecipeDTO> recipes = recipeService.getAllRecipesByUsername(name);

        return ResponseEntity.ok().body(recipes);
    }

    @PostMapping("/addFavorite")
    public @ResponseBody ResponseEntity<String> addRecipeToFavorite(@RequestBody AddFavoriteDTO addFavoriteDTO) {
        recipeService.addFavoriteRecipe(addFavoriteDTO);

        return ResponseEntity.ok().body("Favorite list updated.");
    }

    @PostMapping("/all/filtered")
    public @ResponseBody ResponseEntity<List<ResponseRecipeDTO>> filterAllRecipesByIngredients(@RequestBody FilterRecipeDTO recipeFilter) {
        return ResponseEntity.ok().body(recipeService.filterAllRecipesByIngredients(recipeFilter));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteById(@RequestParam Long id) {
        recipeService.deleteRecipe(id);

        return ResponseEntity.status(HttpStatus.OK).body("Recipe with id " + id + " was deleted.");
    }
}
