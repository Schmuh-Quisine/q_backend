package org.schmuh.quisine.services;

import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.schmuh.quisine.dto.IngredientDto;
import org.schmuh.quisine.dto.RecipeDto;
import org.schmuh.quisine.entity.Ingredient;
import org.schmuh.quisine.entity.Recipe;
import org.schmuh.quisine.entity.RecipeIngredient;
import org.schmuh.quisine.entity.Tag;
import org.schmuh.quisine.repository.IngredientRepository;
import org.schmuh.quisine.repository.RecipeRepository;
import org.schmuh.quisine.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private TagRepository tagRepository;


    public RecipeDto getRecipeById(Long id) {
       return this.mapToDto(Objects.requireNonNull(recipeRepository.findById(id).orElse(null)));
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

    public Recipe saveRecipe(RecipeDto recipeDto) {
        // Map Recipe
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setPersonAmount(recipeDto.getPersonAmount());
        recipe.setTimeEffort(recipeDto.getTimeEffort());
        recipe.setInstructions(recipeDto.getInstructions());
        recipe.setImageSrc(recipeDto.getImgSrc());

        // Map Tags
        Set<Tag> tags = recipeDto.getTags().stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseGet(() -> {
                            Tag tag = new Tag();
                            tag.setName(tagName);
                            return tagRepository.save(tag);
                        }))
                .collect(Collectors.toSet());
        recipe.setTags(tags);

        // Map Ingredients
        Set<RecipeIngredient> recipeIngredients = recipeDto.getIngredients().stream()
                .map(ingredientDto -> {
                    Ingredient ingredient = ingredientRepository.findByName(ingredientDto.getName())
                            .orElseGet(() -> {
                                Ingredient newIngredient = new Ingredient();
                                newIngredient.setName(ingredientDto.getName());
                                return ingredientRepository.save(newIngredient);
                            });

                    RecipeIngredient recipeIngredient = new RecipeIngredient();
                    recipeIngredient.setIngredient(ingredient);
                    recipeIngredient.setRecipe(recipe);
                    recipeIngredient.setAmount(ingredientDto.getAmount());
                    recipeIngredient.setUnit(ingredientDto.getUnit());
                    return recipeIngredient;
                }).collect(Collectors.toSet());
        recipe.setIngredients(recipeIngredients);

        // Save Recipe
        return recipeRepository.save(recipe);
    }

    public RecipeDto mapToDto(Recipe recipe) {
        RecipeDto dto = new RecipeDto();
        dto.setTitle(recipe.getTitle());
        dto.setDescription(recipe.getDescription());
        dto.setPersonAmount(recipe.getPersonAmount());
        dto.setTimeEffort(recipe.getTimeEffort());
        dto.setInstructions(recipe.getInstructions());

        // Map Tags
        List<String> tags = recipe.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        dto.setTags(tags);

        // Map Ingredients
        List<IngredientDto> ingredients = recipe.getIngredients().stream()
                .map(this::mapIngredientToDto)
                .collect(Collectors.toList());
        dto.setIngredients(ingredients);
        return dto;
    }

    private IngredientDto mapIngredientToDto(RecipeIngredient recipeIngredient) {
        IngredientDto dto = new IngredientDto();
        dto.setName(recipeIngredient.getIngredient().getName());
        dto.setAmount(recipeIngredient.getAmount());
        dto.setUnit(recipeIngredient.getUnit());
        return dto;
    }

}
