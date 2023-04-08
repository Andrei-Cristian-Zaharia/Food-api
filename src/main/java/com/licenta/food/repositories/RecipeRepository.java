package com.licenta.food.repositories;

import com.licenta.food.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findByName(String name);

    List<Recipe> findAllByPersonUsername(String username);

    List<Recipe> findAllByIdIn(List<Long> id);

    Integer countByPerson_EmailAddress(String email);

    @Query(value = "SELECT AVG(rate) as avgRate FROM recipes r JOIN person p ON(id_person) WHERE p.emailAddress = ?1",
            nativeQuery = true)
    Integer getAverageRateOfUser(String email);

    boolean existsById(Long id);
}
