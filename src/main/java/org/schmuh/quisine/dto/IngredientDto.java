package org.schmuh.quisine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for representing an ingredient.
 *
 * <p>This class is used to transfer ingredient data between layers of the application.
 * It includes fields for the name, amount, and unit of the ingredient. JSON property
 * annotations are used to map the fields to their corresponding JSON keys.
 *
 * <ul>
 *     <li><b>Name:</b> The name of the ingredient (mapped to the "Name" JSON key).</li>
 *     <li><b>Amount:</b> The quantity of the ingredient (mapped to the "Amount" JSON key).</li>
 *     <li><b>Unit:</b> The unit of measurement for the ingredient (mapped to the "Unit" JSON key).</li>
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
public class IngredientDto {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Amount")
    private double amount;
    @JsonProperty("Unit")
    private String unit;
}