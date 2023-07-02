package com.licenta.food.services;

import com.licenta.food.enums.ObjectType;
import com.licenta.food.enums.RecipeStatus;
import com.licenta.food.enums.RomanianAlphabet;
import com.licenta.food.exceptionHandlers.NotFoundException;
import com.licenta.food.exceptionHandlers.RecipeHandlers.CreateRecipeDifferentSizes;
import com.licenta.food.models.*;
import com.licenta.food.models.createRequestDTO.AddFavoriteDTO;
import com.licenta.food.models.createRequestDTO.CreateRecipeDTO;
import com.licenta.food.models.responseDTO.IngredientOnRecipeResponseDTO;
import com.licenta.food.models.responseDTO.ResponseRecipeDTO;
import com.licenta.food.repositories.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class RecipeService {

    private final PersonService personService;
    private final IngredientService ingredientService;
    private final RecipeIngredientsService recipeIngredientsService;
    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;
    private final ReviewRestTemplateService reviewRestTemplateService;
    private final SavedRecipesService savedRecipesService;

    private void createRecipeIngredientRelations(Recipe recipe, CreateRecipeDTO createRecipeDTO) {

        if (createRecipeDTO.getIngredientNames().size() != createRecipeDTO.getIngredientMeasurements().size()) {
            throw new CreateRecipeDifferentSizes();
        }

        if (createRecipeDTO.getId() != null) {
            recipeIngredientsService.deletePreviousRelations(createRecipeDTO.getId());
        }

        for (int i = 0; i < createRecipeDTO.getIngredientNames().size(); i++) {
            recipeIngredientsService.addIngredientToRecipe(
                    recipe,
                    ingredientService.getIngredientByName(createRecipeDTO.getIngredientNames().get(i)),
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

    public List<ResponseRecipeDTO> getAllFavoriteRecipes(String email) {
        return getAllRecipesWithIds(savedRecipesService.getAllRelationsForUserEmail(email)).stream()
                .map((Recipe recipe) -> {
                    ResponseRecipeDTO recipeDTO = modelMapper.map(recipe, ResponseRecipeDTO.class);
                    recipeDTO.setIngredientList(recipeIngredientsService.getIngredientsForRecipe(recipe.getId()));
                    return recipeDTO;
                }).toList();
    }

    public List<ResponseRecipeDTO> getAllRecipesByUsername(String username) {

        return recipeRepository.findAllByPersonUsername(username).stream()
                .map((Recipe recipe) -> {
                    ResponseRecipeDTO recipeDTO = modelMapper.map(recipe, ResponseRecipeDTO.class);
                    recipeDTO.setIngredientList(recipeIngredientsService.getIngredientsForRecipe(recipe.getId()));
                    return recipeDTO;
                }).toList();
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

    public List<String> getAllFavoriteRecipesNames(String email) {
        return getAllRecipesWithIds(savedRecipesService.getAllRelationsForUserEmail(email)).stream()
                .map(Recipe::getName).toList();
    }

    public List<Recipe> getAllRecipesWithIds(List<Long> ids) {
        return recipeRepository.findAllByIdIn(ids);
    }

    public class RecipeMissingIngredientsComparator implements Comparator<ResponseRecipeDTO> {
        @Override
        public int compare(ResponseRecipeDTO recipe1, ResponseRecipeDTO recipe2) {
            return recipe1.getMissingIngredients().compareTo(recipe2.getMissingIngredients());
        }
    }

    public List<ResponseRecipeDTO> filterAllRecipes(FilterRecipeDTO filters) {
        return filterRecipes(filters, recipeRepository.findAll(), false, null);
    }

    public List<ResponseRecipeDTO> filterAllFavoriteRecipes(FilterRecipeDTO filters, String email) {
        return filterRecipes(filters, getAllRecipesWithIds(savedRecipesService.getAllRelationsForUserEmail(email)), true, email);
    }

    public List<ResponseRecipeDTO> filterRecipes(FilterRecipeDTO filters, List<Recipe> recipes, Boolean isFavorite, String email) {

        List<ResponseRecipeDTO> filterResult;
        if (filters.getIngredientsNames() != null && !filters.getIngredientsNames().isEmpty()) {
            filterResult = new java.util.ArrayList<>(recipes.stream()
                    .filter((Recipe recipe) -> {
                        List<String> recipeIngredients = recipeIngredientsService.getIngredientsForRecipe(recipe.getId())
                                .stream()
                                .map(r -> RomanianAlphabet.changedWord(r.getName()))
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
            if (Boolean.TRUE.equals(isFavorite)) {
                filterResult = getAllFavoriteRecipes(email);
            } else {
                filterResult = getAllRecipes();
            }
        }

        return filterResult.stream().filter(r -> {
            if (filters.getDifficulty() != null
                    && filters.getDifficulty() != 0
                    && filters.getDifficulty() != r.getDifficulty()) {
                return false;
            }

            if (filters.getSpiciness() != null
                    && filters.getSpiciness() != 0
                    && filters.getSpiciness() != r.getSpiciness()) {
                return false;
            }

            if (filters.getPrepareTimeMax() != null
                    && filters.getPrepareTimeMin() != null
                    && (filters.getPrepareTimeMax() < r.getTime() || filters.getPrepareTimeMin() > r.getTime())) {
                return false;
            }

            if (filters.getRecipeName() != null
                    && !filters.getRecipeName().isEmpty()
                    && !r.getName().toLowerCase(Locale.getDefault())
                    .contains(filters.getRecipeName().toLowerCase(Locale.getDefault()))) {
                return false;
            }

            if (filters.getAuthorName() != null
                    && !filters.getAuthorName().isEmpty()
                    && !r.getPerson().getUsername().toLowerCase(Locale.getDefault())
                    .contains(filters.getAuthorName().toLowerCase(Locale.getDefault()))) {
                return false;
            }

            if (filters.getStatus() != null
                    && !filters.getStatus().isEmpty()
                    && !r.getStatus().equals(filters.getStatus())) {
                return false;
            }

            if (filters.getRating() != null && filters.getRating() != 0) {
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

    public Boolean addFavoriteRecipe(AddFavoriteDTO addFavoriteDTO) {
        if (Boolean.FALSE.equals(personService.existPersonById(addFavoriteDTO.getUserId()))) {
            throw new NotFoundException(ObjectType.PERSON, addFavoriteDTO.getUserId());
        }

        if (recipeRepository.existsById(addFavoriteDTO.getRecipeId())) {
            savedRecipesService.existItem(addFavoriteDTO);

            savedRecipesService.saveNewRelation(addFavoriteDTO.getUserId(), addFavoriteDTO.getRecipeId());
            return true;
        }

        throw new NotFoundException(ObjectType.RECIPE, addFavoriteDTO.getRecipeId());
    }

    public Integer countUserRecipes(String email) {
        return recipeRepository.countByPerson_EmailAddress(email);
    }

    public List<Recipe> getAllPossibleRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe saveRecipeStatus(RecipeStatusDTO dto) {
        Recipe recipe = getRecipeById(dto.getId());

        if (dto.getStatus().equals("APPROVED")) {
            recipe.setStatus(RecipeStatus.APPROVED.toString());
        } else if (dto.getStatus().equals("DENIED")) {
            recipe.setStatus(RecipeStatus.DENIED.toString());
        } else {
            recipe.setStatus(RecipeStatus.WAITING.toString());
        }

        return recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe createRecipe(CreateRecipeDTO createRecipeDTO) {

        Recipe recipe;
        if (createRecipeDTO.getId() == null) {
            recipe = new Recipe();
        } else {
            recipe = recipeRepository.findById(createRecipeDTO.getId()).get();
        }

        recipe.setName(createRecipeDTO.getName());
        recipe.setDescription(createRecipeDTO.getDescription());
        recipe.setHowToPrepare(createRecipeDTO.getHowToPrepare());
        recipe.setTime(createRecipeDTO.getTime());
        recipe.setDifficulty(createRecipeDTO.getDifficulty());
        recipe.setSpiciness(createRecipeDTO.getSpiciness());
        recipe.setVegan(createRecipeDTO.getIsVegan());
        recipe.setPublicRecipe(createRecipeDTO.getPublicRecipe());
        recipe.setImageAddress(createRecipeDTO.getImageAddress());
        recipe.setPerson(personService.getPersonByName(createRecipeDTO.getOwnerName()));
        recipe.setStatus(RecipeStatus.WAITING.toString());
        recipe = recipeRepository.save(recipe);

        createRecipeIngredientRelations(recipe, createRecipeDTO);

        return recipe;
    }

    @Transactional
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
}
