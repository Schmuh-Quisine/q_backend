package org.schmuh.quisine.repository;

import org.schmuh.quisine.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository extends JpaRepository<Ingredient, Long> {

}
