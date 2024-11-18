package org.schmuh.quisine.repository;

import org.schmuh.quisine.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    public Optional<Recipe> findById(int id);
    public Optional<Recipe> findByName(String name);
}
