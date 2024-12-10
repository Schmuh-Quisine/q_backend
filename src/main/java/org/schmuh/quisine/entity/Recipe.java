package org.schmuh.quisine.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity class representing a recipe in the recipe system.
 *
 * <p>This class is mapped to the "Recipe" table in the database, where each record
 * represents a recipe. The class uses JPA annotations to define the mapping
 * between the entity and the database table.
 *
 * <ul>
 *     <li><b>id:</b> The unique identifier for the recipe (primary key). Generated automatically.</li>
 *     <li><b>title:</b> The title of the recipe.</li>
 *     <li><b>description:</b> A detailed description of the recipe (stored as a large object).</li>
 *     <li><b>tags:</b> A set of {@link Tag} objects representing the tags associated with the recipe.
 *         This is a many-to-many relationship.</li>
 *     <li><b>ingredients:</b> A set of {@link RecipeIngredient} objects representing the ingredients
 *         used in the recipe. This is a one-to-many relationship, where each recipe can have multiple ingredients.</li>
 *     <li><b>personAmount:</b> The number of servings the recipe yields.</li>
 *     <li><b>timeEffort:</b> The estimated time required to prepare the recipe, in minutes.</li>
 *     <li><b>instructions:</b> Step-by-step instructions for preparing the recipe (stored as a large object).</li>
 *     <li><b>imageSrc:</b> The source path or URL for the recipe's image (stored as a large object).</li>
 * </ul>
 *
 * <p>Annotations:
 * <ul>
 *     <li>{@link Entity} - Marks this class as a JPA entity that will be mapped to a database table.</li>
 *     <li>{@link Table} - Specifies the name of the database table ("Recipe") and the unique constraint
 *         on the "id" column to ensure it is unique.</li>
 *     <li>{@link Id} - Marks the primary key field of the entity.</li>
 *     <li>{@link GeneratedValue} - Specifies the strategy for generating the ID value (auto-incremented).</li>
 *     <li>{@link LOB} - Marks fields as large objects (used for large text or binary data like descriptions and instructions).</li>
 *     <li>{@link ManyToMany} - Defines a many-to-many relationship between the {@link Recipe} and {@link Tag} entities.</li>
 *     <li>{@link OneToMany} - Defines a one-to-many relationship between the {@link Recipe} and {@link RecipeIngredient} entities.
 *         The relationship is mapped by the "recipe" field in the {@link RecipeIngredient} entity, with cascading operations
 *         and orphan removal enabled.</li>
 * </ul>
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Recipe", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String description;
    @ManyToMany()
    private Set<Tag> tags;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecipeIngredient> ingredients;
    private int personAmount;
    private int timeEffort;
    @Lob
    private String instructions;
    @Lob
    private String imageSrc;

}
