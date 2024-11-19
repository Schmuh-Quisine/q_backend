package org.schmuh.quisine.repository;

import org.schmuh.quisine.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    public Optional<Recipe> findById(Long id);
    public Optional<Recipe> findByTitle(String title);
}
