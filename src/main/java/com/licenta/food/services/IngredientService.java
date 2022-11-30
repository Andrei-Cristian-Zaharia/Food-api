package com.licenta.food.services;

import com.licenta.food.enums.ObjectType;
import com.licenta.food.exceptionHandlers.AlreadyExistsException;
import com.licenta.food.exceptionHandlers.NotFoundException;
import com.licenta.food.models.createRequestDTO.CreateIngredientRequest;
import com.licenta.food.models.Ingredient;
import com.licenta.food.repositories.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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

        ingredients.forEach((Optional<Ingredient> ingredient) -> {
            if (ingredient.isEmpty()) {
                throw new NotFoundException("There are ingredients in this list that are not found !");
            }
        });

        return ingredients.stream().map(Optional::get).toList();
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
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
