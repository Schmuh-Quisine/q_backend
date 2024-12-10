package org.schmuh.quisine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Entity class representing an ingredient in the recipe system.
 *
 * <p>This class is mapped to the "Ingredient" table in the database, where each record
 * represents a unique ingredient. The class uses JPA annotations to define the mapping
 * between the entity and the database table.
 *
 * <ul>
 *     <li><b>id:</b> The unique identifier for the ingredient (primary key). Generated automatically.</li>
 *     <li><b>name:</b> The name of the ingredient, which is required to be unique.</li>
 *     <li><b>recipeIngredients:</b> A set of {@link RecipeIngredient} objects representing
 *         the relationships between this ingredient and the recipes it is part of. This is a one-to-many
 *         relationship, where an ingredient can appear in multiple recipes.</li>
 * </ul>
 *
 * <p>Annotations:
 * <ul>
 *     <li>{@link Entity} - Marks this class as a JPA entity that will be mapped to a database table.</li>
 *     <li>{@link Table} - Specifies the name of the database table ("Ingredient") and the unique constraint
 *         on the "name" column to ensure that ingredient names are unique.</li>
 *     <li>{@link Id} - Marks the primary key field of the entity.</li>
 *     <li>{@link GeneratedValue} - Specifies the strategy for generating the ID value (auto-incremented).</li>
 *     <li>{@link Column} - Defines how the fields map to columns in the database table.</li>
 *     <li>{@link OneToMany} - Defines a one-to-many relationship between the {@link Ingredient} entity and the
 *         {@link RecipeIngredient} entity. The relationship is mapped by the "ingredient" field in
 *         the {@link RecipeIngredient} entity, with cascading operations and orphan removal enabled.</li>
 * </ul>
 */
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="Ingredient", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column
    public String name;
    @Column
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecipeIngredient> recipeIngredients;

}
