package org.schmuh.quisine.repository;

import org.schmuh.quisine.entity.Ingredient;
import org.schmuh.quisine.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByName(String name);
}
