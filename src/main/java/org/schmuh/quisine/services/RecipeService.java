package org.schmuh.quisine.services;

import lombok.NoArgsConstructor;
import org.schmuh.quisine.entity.Recipe;
import org.schmuh.quisine.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;


    public Recipe getRecipeById(int id) {
        return this.recipeRepository.findById(id).orElse(null);
    }

    public void saveRecipe(Recipe recipe) {
        this.recipeRepository.save(recipe);

    }
}
