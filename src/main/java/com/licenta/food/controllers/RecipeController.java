package com.licenta.food.controllers;

import com.licenta.food.models.FilterRecipeDTO;
import com.licenta.food.models.Recipe;
import com.licenta.food.models.createRequestDTO.CreateRecipeDTO;
import com.licenta.food.models.responseDTO.ResponseRecipeDTO;
import com.licenta.food.services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public @ResponseBody ResponseEntity<Recipe> createNewRecipe(@RequestBody CreateRecipeDTO createRecipeDTO) {
        Recipe recipe = recipeService.createRecipe(createRecipeDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(recipe);
    }

    @GetMapping("id/{id}")
    public @ResponseBody ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Recipe result = recipeService.getRecipeById(id);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/info/id/{id}")
    public @ResponseBody ResponseEntity<ResponseRecipeDTO> getRecipeInfoById(@PathVariable Long id) {
        ResponseRecipeDTO result = recipeService.getRecipeInfoById(id);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/name/{name}")
    public @ResponseBody ResponseEntity<ResponseRecipeDTO> getRecipeByName(@PathVariable String name) {
        ResponseRecipeDTO result = recipeService.getRecipeByName(name);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/all")
    public @ResponseBody ResponseEntity<List<ResponseRecipeDTO>> getAllRecipes() {
        List<ResponseRecipeDTO> recipes = recipeService.getAllRecipes();

        return ResponseEntity.ok().body(recipes);
    }

    @GetMapping("/all/filterIngredients")
    public @ResponseBody ResponseEntity<List<ResponseRecipeDTO>> filterAllRecipesByIngredients(@RequestParam List<String> recipeFilter) {
        return ResponseEntity.ok().body(recipeService.filterAllRecipesByIngredients(recipeFilter));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteById(@RequestParam Long id) {
        recipeService.deleteRecipe(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Recipe with id " + id + " was deleted.");
    }
}
