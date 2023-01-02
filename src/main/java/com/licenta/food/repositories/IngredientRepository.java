package com.licenta.food.repositories;

import com.licenta.food.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository <Ingredient, Long> {

    Optional<Ingredient> findByName(String name);

    Optional<List<Ingredient>> findAllByCategory(String categoryName);

    Optional<List<Ingredient>> findAllByCategoryAndNameContaining(String categoryName, String nameFilter);

    void deleteByName(String name);

    List<Optional<Ingredient>> findAllByNameIn(List<String> usernames);
}
