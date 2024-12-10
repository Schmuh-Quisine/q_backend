package org.schmuh.quisine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Data Transfer Object (DTO) for representing a recipe.
 *
 * <p>This class is used to transfer recipe data between layers of the application.
 * It includes fields for recipe details such as title, description, ingredients,
 * and other metadata. JSON property annotations are used to map the fields to their
 * corresponding JSON keys for serialization and deserialization.
 *
 * <ul>
 *     <li><b>Id:</b> The unique identifier of the recipe (mapped to the "Id" JSON key).</li>
 *     <li><b>Title:</b> The title of the recipe (mapped to the "Title" JSON key).</li>
 *     <li><b>Description:</b> A brief description of the recipe (mapped to the "Description" JSON key).</li>
 *     <li><b>Ingredients:</b> A list of ingredients for the recipe, represented by {@link IngredientDto}
 *         objects (mapped to the "Ingredients" JSON key).</li>
 *     <li><b>PersonAmount:</b> The number of servings the recipe yields (mapped to the "PersonAmount" JSON key).</li>
 *     <li><b>TimeEffort:</b> The estimated time required to prepare the recipe in minutes (mapped to the "TimeEffort" JSON key).</li>
 *     <li><b>Tags:</b> A list of tags or keywords associated with the recipe (mapped to the "Tags" JSON key).</li>
 *     <li><b>Instructions:</b> Step-by-step instructions for preparing the recipe (mapped to the "Instructions" JSON key).</li>
 *     <li><b>ImgSrc:</b> The source path or URL for an optional image of the recipe (mapped to the "ImgSrc" JSON key).</li>
 * </ul>
 *
 * <p>This class uses Lombok annotations to generate boilerplate code like constructors,
 * getters, and setters.
 *
 * <p>Annotations:
 * <ul>
 *     <li>{@link NoArgsConstructor} - Generates a no-argument constructor.</li>
 *     <li>{@link AllArgsConstructor} - Generates a constructor with arguments for all fields.</li>
 *     <li>{@link Getter} - Generates getter methods for all fields.</li>
 *     <li>{@link Setter} - Generates setter methods for all fields.</li>
 *     <li>{@link JsonProperty} - Maps fields to JSON keys for serialization and deserialization.</li>
 * </ul>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecipeDto {
    @JsonProperty("Id")
    private long id;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Ingredients")
    private List<IngredientDto> ingredients;
    @JsonProperty("PersonAmount")
    private int personAmount;
    @JsonProperty("TimeEffort")
    private int timeEffort;
    @JsonProperty("Tags")
    private List<String> tags;
    @JsonProperty("Instructions")
    private String instructions;
    @JsonProperty("ImgSrc")
    private String imgSrc; // Optional field for recipe images
}