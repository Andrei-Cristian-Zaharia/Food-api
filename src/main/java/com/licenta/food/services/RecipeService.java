package com.licenta.food.services;

import com.licenta.food.enums.MeasurementUnit;
import com.licenta.food.enums.ObjectType;
import com.licenta.food.exceptionHandlers.NotFoundException;
import com.licenta.food.exceptionHandlers.RecipeHandlers.CreateRecipeDifferentSizes;
import com.licenta.food.models.*;
import com.licenta.food.models.createRequestDTO.CreateRecipeDTO;
import com.licenta.food.models.responseDTO.ResponseRecipeDTO;
import com.licenta.food.repositories.RecipeRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final PersonService personService;

    private final IngredientService ingredientService;

    private final RecipeIngredientsService recipeIngredientsService;

    private final RecipeRepository recipeRepository;

    private final ModelMapper modelMapper;

    public RecipeService(PersonService personService, IngredientService ingredientService,
                         RecipeIngredientsService recipeIngredientsService,
                         RecipeRepository recipeRepository, ModelMapper modelMapper) {
        this.personService = personService;
        this.ingredientService = ingredientService;
        this.recipeIngredientsService = recipeIngredientsService;
        this.recipeRepository = recipeRepository;
        this.modelMapper = modelMapper;
    }

    private void createRecipeIngredientRelations(Recipe recipe, CreateRecipeDTO createRecipeDTO) {

        if (!(createRecipeDTO.getIngredientNames().size() == createRecipeDTO.getIngredientQuantities().size()
            && createRecipeDTO.getIngredientQuantities().size() == createRecipeDTO.getIngredientMeasurements().size())){
            throw new CreateRecipeDifferentSizes();
        }

        List<Ingredient> ingredients
                = ingredientService.getAllIngredientsWithNames(createRecipeDTO.getIngredientNames());

        for (int i = 0; i < ingredients.size(); i++) {
            if (Boolean.FALSE.equals(MeasurementUnit.contains(createRecipeDTO.getIngredientMeasurements().get(i)))) {
                throw new NotFoundException(ObjectType.MEASUREMENT, createRecipeDTO.getIngredientMeasurements().get(i));
            }

            recipeIngredientsService.addIngredientToRecipe(
                    recipe,
                    ingredients.get(i),
                    createRecipeDTO.getIngredientQuantities().get(i),
                    createRecipeDTO.getIngredientMeasurements().get(i)
            );
        }
    }

    public ResponseRecipeDTO getRecipeByName(String name) {

        Optional<Recipe> recipe = recipeRepository.findByName(name);

        if (recipe.isPresent()) {
            ResponseRecipeDTO recipeDTO = modelMapper.map(recipe, ResponseRecipeDTO.class);
            recipeDTO.setIngredientList(recipeIngredientsService.getIngredientsForRecipe(recipe.get().getId()));

            return recipeDTO;
        } else {
            throw new NotFoundException(ObjectType.RECIPE, name);
        }
    }

    public ResponseRecipeDTO getRecipeById(Long id) {

        Optional<Recipe> recipe = recipeRepository.findById(id);

        if (recipe.isPresent()) {
            ResponseRecipeDTO recipeDTO = modelMapper.map(recipe, ResponseRecipeDTO.class);
            recipeDTO.setIngredientList(recipeIngredientsService.getIngredientsForRecipe(recipe.get().getId()));

            return recipeDTO;
        } else {
            throw new NotFoundException(ObjectType.RECIPE, id);
        }
    }

    public List<ResponseRecipeDTO> getAllRecipes() {

        return recipeRepository.findAll().stream()
                .map((Recipe recipe) -> {
                    ResponseRecipeDTO recipeDTO = modelMapper.map(recipe, ResponseRecipeDTO.class);
                    recipeDTO.setIngredientList(recipeIngredientsService.getIngredientsForRecipe(recipe.getId()));
                    return recipeDTO;
                })
                .toList();
    }

    @Transactional
    public Recipe createRecipe(CreateRecipeDTO createRecipeDTO) {

        Recipe recipe = new Recipe();
        recipe.setName(createRecipeDTO.getName());
        recipe.setDescription(createRecipeDTO.getDescription());
        recipe.setHowToPrepare(createRecipeDTO.getHowToPrepare());
        recipe.setPerson(personService.getPersonByName(createRecipeDTO.getOwnerName()));
        recipe = recipeRepository.save(recipe);

        createRecipeIngredientRelations(recipe, createRecipeDTO);

        return recipe;
    }

    @Transactional
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
}
