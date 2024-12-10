package org.schmuh.quisine.repository;

import org.schmuh.quisine.entity.Ingredient;
import org.schmuh.quisine.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Ingredient} entities.
 *
 * <p>This interface extends {@link JpaRepository}, providing CRUD operations and query methods for
 * {@link Ingredient} entities. It includes a custom method for finding ingredients by their name.
 *
 * <ul>
 *     <li>{@link JpaRepository} - Provides basic CRUD operations such as saving, deleting, and finding entities.</li>
 * </ul>
 *
 * <p>Methods:
 * <ul>
 *     <li><b>findByName:</b> Retrieves an {@link Ingredient} based on its name. Returns an {@link Optional} to
 *         handle cases where the ingredient may not exist in the database.</li>
 * </ul>
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByName(String name);
}
