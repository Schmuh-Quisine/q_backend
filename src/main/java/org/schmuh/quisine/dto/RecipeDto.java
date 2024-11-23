package org.schmuh.quisine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecipeDto {
    @JsonProperty("Id")
    private int id;
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

    // Getters and Setters
}