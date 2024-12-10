package org.schmuh.quisine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class representing the relationship between a recipe and its ingredients.
 *
 * <p>This class is mapped to a join table that represents the many-to-many relationship
 * between recipes and ingredients. Each instance of this entity links a recipe with an ingredient
 * and specifies the amount and unit of the ingredient used in the recipe.
 *
 * <ul>
 *     <li><b>id:</b> The unique identifier for the recipe-ingredient relationship (primary key).
 *         Generated automatically.</li>
 *     <li><b>recipe:</b> The {@link Recipe} associated with this ingredient in the recipe.
 *         This is a many-to-one relationship with the {@link Recipe} entity.</li>
 *     <li><b>ingredient:</b> The {@link Ingredient} used in the recipe. This is a many-to-one relationship
 *         with the {@link Ingredient} entity.</li>
 *     <li><b>amount:</b> The quantity of the ingredient used in the recipe.</li>
 *     <li><b>unit:</b> The unit of measurement for the ingredient (e.g., grams, cups, tablespoons).</li>
 * </ul>
 *
 * <p>Annotations:
 * <ul>
 *     <li>{@link Entity} - Marks this class as a JPA entity that will be mapped to a database table.</li>
 *     <li>{@link Id} - Marks the primary key field of the entity.</li>
 *     <li>{@link GeneratedValue} - Specifies the strategy for generating the ID value (auto-incremented).</li>
 *     <li>{@link ManyToOne} - Defines a many-to-one relationship between {@link RecipeIngredient} and the
 *         {@link Recipe} and {@link Ingredient} entities. The relationships are mapped via the
 *         {@link JoinColumn} annotation which specifies the foreign key columns ("recipe_id" and "ingredient_id").</li>
 *     <li>{@link JoinColumn} - Specifies the foreign key column names used to establish the relationships with
 *         the {@link Recipe} and {@link Ingredient} entities.</li>
 * </ul>
 */

@Getter
@Setter
@Entity
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private double amount;


    private String unit;

}