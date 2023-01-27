package com.licenta.food.services;

import com.licenta.food.enums.ObjectType;
import com.licenta.food.exceptionHandlers.NotFoundException;
import com.licenta.food.exceptionHandlers.RecipeHandlers.CreateRecipeDifferentSizes;
import com.licenta.food.models.*;
import com.licenta.food.models.createRequestDTO.CreateRecipeDTO;
import com.licenta.food.models.responseDTO.IngredientOnRecipeResponseDTO;
import com.licenta.food.models.responseDTO.ResponseRecipeDTO;
import com.licenta.food.repositories.RecipeRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final PersonService personService;

    private final IngredientService ingredientService;

    private final RecipeIngredientsService recipeIngredientsService;

    private final RecipeRepository recipeRepository;

    private final ModelMapper modelMapper;
    private final ReviewRestTemplateService reviewRestTemplateService;

    public RecipeService(PersonService personService, IngredientService ingredientService,
                         RecipeIngredientsService recipeIngredientsService,
                         RecipeRepository recipeRepository, ModelMapper modelMapper,
                         ReviewRestTemplateService reviewRestTemplateService) {
        this.personService = personService;
        this.ingredientService = ingredientService;
        this.recipeIngredientsService = recipeIngredientsService;
        this.recipeRepository = recipeRepository;
        this.modelMapper = modelMapper;
        this.reviewRestTemplateService = reviewRestTemplateService;
    }

    private void createRecipeIngredientRelations(Recipe recipe, CreateRecipeDTO createRecipeDTO) {

        if (!(createRecipeDTO.getIngredientNames().size() == createRecipeDTO.getIngredientQuantities().size()
                && createRecipeDTO.getIngredientQuantities().size() == createRecipeDTO.getIngredientMeasurements().size())) {
            throw new CreateRecipeDifferentSizes();
        }

        List<Ingredient> ingredients
                = ingredientService.getAllIngredientsWithNames(createRecipeDTO.getIngredientNames());

        for (int i = 0; i < ingredients.size(); i++) {
//            if (Boolean.FALSE.equals(MeasurementUnit.contains(createRecipeDTO.getIngredientMeasurements().get(i)))) {
//                throw new NotFoundException(ObjectType.MEASUREMENT, createRecipeDTO.getIngredientMeasurements().get(i));
//            }

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

    public Recipe getRecipeById(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);

        if (recipe.isPresent()) {
            return recipe.get();
        } else {
            throw new NotFoundException(ObjectType.RECIPE, id);
        }
    }

    public ResponseRecipeDTO getRecipeInfoById(Long id) {

        Optional<Recipe> recipe = recipeRepository.findById(id);

        if (recipe.isPresent()) {
            ResponseRecipeDTO recipeDTO = modelMapper.map(recipe, ResponseRecipeDTO.class);
            recipeDTO.setIngredientList(recipeIngredientsService.getIngredientsForRecipe(recipe.get().getId()));

            return recipeDTO;
        } else {
            throw new NotFoundException(ObjectType.RECIPE, id);
        }
    }

    public class RecipeMissingIngredientsComparator implements Comparator<ResponseRecipeDTO> {
        @Override
        public int compare(ResponseRecipeDTO recipe1, ResponseRecipeDTO recipe2) {
            return recipe1.getMissingIngredients().compareTo(recipe2.getMissingIngredients());
        }
    }

    public List<ResponseRecipeDTO> filterAllRecipesByIngredients(FilterRecipeDTO filters) {

        List<ResponseRecipeDTO> filterResult;
        if (filters.getIngredientsNames() != null && !filters.getIngredientsNames().isEmpty()) {
            filterResult = new java.util.ArrayList<>(recipeRepository.findAll().stream()
                    .filter((Recipe recipe) -> {
                        List<String> recipeIngredients = recipeIngredientsService.getIngredientsForRecipe(recipe.getId())
                                .stream()
                                .map(IngredientOnRecipeResponseDTO::getName)
                                .toList();

                        for (String ingredientName : filters.getIngredientsNames()) {
                            if (recipeIngredients.contains(ingredientName)) {
                                return true;
                            }
                        }

                        return false;
                    }).map((Recipe recipe) -> {
                        ResponseRecipeDTO recipeDTO = modelMapper.map(recipe, ResponseRecipeDTO.class);
                        recipeDTO.setIngredientList(recipeIngredientsService.getIngredientsForRecipe(recipe.getId()));

                        List<String> commonIngredients = new java.util.ArrayList<>(recipeDTO.getIngredientList().stream()
                                .map(IngredientOnRecipeResponseDTO::getName)
                                .toList());

                        commonIngredients.retainAll(filters.getIngredientsNames());

                        recipeDTO.setMissingIngredients(recipeDTO.getIngredientList().size() - commonIngredients.size());
                        return recipeDTO;
                    }).toList());
        } else {
            filterResult = getAllRecipes();
        }

        return filterResult.stream().filter(r -> {
            if (filters.getDifficulty() != 0 && filters.getDifficulty() != r.getDifficulty()) {
                return false;
            }

            if (filters.getSpiciness() != 0 && filters.getSpiciness() != r.getSpiciness()) {
                return false;
            }

            if (filters.getPrepareTimeMax() < r.getTime() || filters.getPrepareTimeMin() > r.getTime()) {
                return false;
            }

            if (filters.getRecipeName() != null
                    && !filters.getRecipeName().isEmpty()
                    && !r.getName().contains(filters.getRecipeName())) {
                return false;
            }

            if (filters.getRating() != 0) {
                try {
                    Integer rating = reviewRestTemplateService.getRatingForRecipe(r.getId());

                    if (!Objects.equals(rating, filters.getRating())) {
                        return false;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            return true;
        }).sorted(Comparator.comparing(ResponseRecipeDTO::getMissingIngredients)).toList();
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
        recipe.setTime(createRecipeDTO.getTime());
        recipe.setDifficulty(createRecipeDTO.getDifficulty());
        recipe.setSpiciness(createRecipeDTO.getSpiciness());
        recipe.setVegan(createRecipeDTO.getIsVegan());
        recipe.setImageAddress(createRecipeDTO.getImageAddress());
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
