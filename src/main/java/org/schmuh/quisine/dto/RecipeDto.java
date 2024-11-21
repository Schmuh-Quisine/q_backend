package org.schmuh.quisine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecipeDto {
    private String Title;
    private String Description;
    private List<IngredientDto> Ingredients;
    private int PersonAmount;
    private int TimeEffort;
    private List<String> Tags;
    private String Instructions;
    private String ImgSrc; // Optional field for recipe images

    // Getters and Setters
}