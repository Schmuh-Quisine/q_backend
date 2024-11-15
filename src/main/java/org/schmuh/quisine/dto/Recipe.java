package org.schmuh.quisine.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    public int id;
    public String name;
    public String description;
    public String image;
    public String steps;
    public List<Ingredients> ingredientList;
    public int personAmount;
    public int durationInMinute;
    public List<Tag> tagList;




}
