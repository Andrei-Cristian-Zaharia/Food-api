package com.licenta.food.services;

import com.licenta.food.enums.IngredientCategory;
import com.licenta.food.enums.ObjectType;
import com.licenta.food.exceptionHandlers.AlreadyExistsException;
import com.licenta.food.exceptionHandlers.NotFoundException;
import com.licenta.food.models.CategoryIngredientsDTO;
import com.licenta.food.models.IngredientByCategoryDTO;
import com.licenta.food.models.createRequestDTO.CreateIngredientRequest;
import com.licenta.food.models.Ingredient;
import com.licenta.food.repositories.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient getIngredientByName(String name) {

        Optional<Ingredient> ingredient = ingredientRepository.findByName(name);

        return ingredient.orElseThrow(() -> new NotFoundException(ObjectType.INGREDIENT, name));
    }

    public List<Ingredient> getIngredientsByCategory(String categoryName) {

        Optional<List<Ingredient>> ingredientList = ingredientRepository.findAllByCategory(categoryName);

        return ingredientList.orElseThrow(() ->
                new NotFoundException("Ingredients for category " + categoryName + " was not found !")
        );
    }

    public List<Ingredient> getAllIngredientsWithNames(List<String> ingredientNames) {

        List<Optional<Ingredient>> ingredients = ingredientRepository.findAllByNameIn(ingredientNames);

        if (ingredientNames.size() != ingredients.size()) {
            throw new NotFoundException("There are ingredients in this list that are not found !");
        }

        return ingredients.stream().map(Optional::get).toList();
    }

    public IngredientByCategoryDTO getAllIngredientsByCategory() {

        IngredientByCategoryDTO byCategoryList = new IngredientByCategoryDTO();

        IngredientCategory.getCategories().forEach((String category) -> {
            CategoryIngredientsDTO categoryIngredients = new CategoryIngredientsDTO();

            categoryIngredients.setCategoryName(category);
            categoryIngredients.setIngredients(ingredientRepository.findAllByCategory(category).get());
            byCategoryList.addCategory(categoryIngredients);
        });

        return byCategoryList;
    }

    public IngredientByCategoryDTO getAllIngredientsByCategoryFiltered(String nameFilter) {

        IngredientByCategoryDTO byCategoryList = new IngredientByCategoryDTO();

        IngredientCategory.getCategories().forEach((String category) -> {
            CategoryIngredientsDTO categoryIngredients = new CategoryIngredientsDTO();

            categoryIngredients.setCategoryName(category);
            categoryIngredients.setIngredients(
                    ingredientRepository.findAllByCategoryAndNameContaining(category, nameFilter).get());
            if (!categoryIngredients.getIngredients().isEmpty()) {
                byCategoryList.addCategory(categoryIngredients);
            }
        });

        return byCategoryList;
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public List<String> getAllIngredientsNames() {
        return ingredientRepository.findAll().stream().map(Ingredient::getName).toList();
    }

    @Transactional
    public Ingredient addIngredient(CreateIngredientRequest createIngredientRequest) {

        if (ingredientRepository.findByName(createIngredientRequest.getName()).isPresent()) {
            throw new AlreadyExistsException();
        }

        Ingredient ingredient = new Ingredient();

        ingredient.setName(createIngredientRequest.getName());
        ingredient.setCategory(createIngredientRequest.getCategory());

        return ingredientRepository.save(ingredient);
    }

    @Transactional
    public void deleteIngredientByName(String name) {

        Optional<Ingredient> ingredient = ingredientRepository.findByName(name);

        if (ingredient.isPresent()) {
            ingredientRepository.deleteByName(name);
        } else {
            throw new NotFoundException("The ingredient you are trying ti delete, does not exists.");
        }
    }
}
