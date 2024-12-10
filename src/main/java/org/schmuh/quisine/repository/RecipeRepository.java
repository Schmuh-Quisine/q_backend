package org.schmuh.quisine.repository;

import org.schmuh.quisine.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Recipe} entities.
 *
 * <p>This interface extends {@link JpaRepository}, providing CRUD operations for managing
 * {@link Recipe} entities. It includes custom methods for retrieving recipes by their ID or title.
 *
 * <ul>
 *     <li>{@link JpaRepository} - Provides basic CRUD operations such as saving, deleting, and finding entities.</li>
 * </ul>
 *
 * <p>Methods:
 * <ul>
 *     <li><b>findById:</b> Retrieves a {@link Recipe} by its ID. Returns an {@link Optional} to handle cases
 *         where the recipe may not exist in the database.</li>
 *     <li><b>findByTitle:</b> Retrieves a {@link Recipe} by its title. Returns an {@link Optional} to handle
 *         cases where the recipe with the specified title may not exist.</li>
 * </ul>
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    public Optional<Recipe> findById(Long id);
    public Optional<Recipe> findByTitle(String title);
}
