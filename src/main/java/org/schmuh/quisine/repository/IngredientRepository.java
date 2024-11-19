package org.schmuh.quisine.repository;

import org.schmuh.quisine.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Recipe, Long> {

}
