package org.schmuh.quisine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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