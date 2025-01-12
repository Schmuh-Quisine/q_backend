package org.schmuh.quisine.repository;

import org.schmuh.quisine.entity.Ingredient;
import org.schmuh.quisine.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link RecipeIngredient} entities.
 *
 * <p>This interface extends {@link JpaRepository}, providing CRUD operations for managing the
 * {@link RecipeIngredient} entity, which represents the relationship between recipes and their ingredients.
 * The repository allows operations such as saving, deleting, and querying {@link RecipeIngredient} entities.
 *
 * <p>In this case, the repository is designed for the {@link RecipeIngredient} entity, but it is currently
 * using {@link Ingredient} as the entity type, which should be corrected to {@link RecipeIngredient}.
 *
 * <p>Methods:
 * <ul>
 *     <li>Standard {@link JpaRepository} methods are available, including <code>save()</code>,
 *         <code>findAll()</code>, <code>deleteById()</code>, and more for managing {@link RecipeIngredient} entities.</li>
 * </ul>
 */
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    void removeAllById(Long id);
}
