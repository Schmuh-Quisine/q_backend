package org.schmuh.quisine.services;

import lombok.NoArgsConstructor;
import org.schmuh.quisine.entity.Recipe;
import org.schmuh.quisine.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;


    public Recipe getRecipeById(Long id) {
        return this.recipeRepository.findById(id).orElse(null);
    }

    public void saveRecipe(Recipe recipe) {
        this.recipeRepository.save(recipe);

    }

    public void updateRecipeById(Long id, Recipe recipe) {

        // TODO finish update functionality of recipe
        Recipe tmpRecipe = this.recipeRepository.findById(id).orElse(null);
        tmpRecipe.setDescription(recipe.getDescription());
        this.saveRecipe(tmpRecipe);

    }

    public List<Recipe> findAll() {

        return this.recipeRepository.findAll();
    }
}
