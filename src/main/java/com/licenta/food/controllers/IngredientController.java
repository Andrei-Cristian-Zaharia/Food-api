package com.licenta.food.controllers;

import com.licenta.food.models.IngredientByCategoryDTO;
import com.licenta.food.models.createRequestDTO.CreateIngredientRequest;
import com.licenta.food.models.Ingredient;
import com.licenta.food.services.IngredientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public @ResponseBody ResponseEntity<Ingredient> postIngredient(
            @Valid @RequestBody CreateIngredientRequest createIngredientRequest
    ) {
        Ingredient resultIngredient = ingredientService.addIngredient(createIngredientRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(resultIngredient);
    }

    @GetMapping("/name")
    public @ResponseBody ResponseEntity<Ingredient> getIngredientByName(@Valid @RequestParam String name){
        Ingredient resultIngredient = ingredientService.getIngredientByName(name);

        return ResponseEntity.ok().body(resultIngredient);
    }

    @GetMapping("/category")
    public @ResponseBody ResponseEntity<List<Ingredient>> getAllForCategory(@Valid @RequestParam String categoryName){
        List<Ingredient> ingredientList = ingredientService.getIngredientsByCategory(categoryName);

        return ResponseEntity.ok().body(ingredientList);
    }

    @GetMapping("/all/byCategory")
    public @ResponseBody ResponseEntity<IngredientByCategoryDTO> getAllIngredientsByCategory() {
        return ResponseEntity.ok().body(ingredientService.getAllIngredientsByCategory());
    }

    @GetMapping("/all/byCategory/filter")
    public @ResponseBody ResponseEntity<IngredientByCategoryDTO> getAllIngredientsByCategory(
            @RequestParam String nameFilter) {
        return ResponseEntity.ok().body(ingredientService.getAllIngredientsByCategoryFiltered(nameFilter));
    }

    @GetMapping("/all")
    public @ResponseBody ResponseEntity<List<Ingredient>> getALl() {
        List<Ingredient> ingredientList = ingredientService.getAllIngredients();

        return ResponseEntity.ok().body(ingredientList);
    }

    @GetMapping("/all/name")
    public @ResponseBody ResponseEntity<List<String>> getAllNames() {
        List<String> ingredientList = ingredientService.getAllIngredientsNames();

        return ResponseEntity.ok().body(ingredientList);
    }

    @DeleteMapping("/delete/name/{name}")
    public ResponseEntity<String> deleteByName(@PathVariable String name) {
        ingredientService.deleteIngredientByName(name);

        return ResponseEntity.status(HttpStatus.OK).body("Ingredient " + name + " was deleted.");
    }
}
