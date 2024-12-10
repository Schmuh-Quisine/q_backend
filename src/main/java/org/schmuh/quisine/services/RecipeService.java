package org.schmuh.quisine.services;

import lombok.NoArgsConstructor;
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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * Service class for managing {@link Recipe} entities and their related operations.
 *
 * <p>This service class provides methods for creating, updating, deleting, and retrieving recipes.
 * It interacts with the repository layer for persistence and performs necessary mappings
 * between entity objects and Data Transfer Objects (DTOs).
 */
@Service
@NoArgsConstructor
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private TagRepository tagRepository;

    /**
     * Retrieves a {@link RecipeDto} by its unique ID.
     *
     * <p>This method fetches the {@link Recipe} entity with the specified ID from the database,
     * converts it to a {@link RecipeDto}, and returns the DTO.
     *
     * @param id The unique ID of the recipe.
     * @return A {@link RecipeDto} representing the recipe with the given ID, or {@code null} if not found.
     */
    public RecipeDto getRecipeById(Long id) {
        return this.mapToDto(Objects.requireNonNull(recipeRepository.findById(id).orElse(null)));
    }

    /**
     * Creates and saves a {@link Recipe} entity to the database.
     *
     * <p>This method directly saves a {@link Recipe} object to the database using the repository.
     *
     * @param recipe The {@link Recipe} entity to be created.
     */
    public void createRecipe(Recipe recipe) {
        this.recipeRepository.save(recipe);
    }

    /**
     * Deletes a {@link Recipe} entity by its unique ID.
     *
     * <p>This method removes the recipe with the given ID from the database.
     *
     * @param id The unique ID of the recipe to be deleted.
     */
    public void deleteRecipe(Long id) {
        this.recipeRepository.deleteById(id);
    }

    /**
     * Retrieves all {@link Recipe} entities from the database.
     *
     * <p>This method fetches all recipes from the database and returns them as a list.
     *
     * @return A list of all {@link Recipe} entities.
     */
    public List<Recipe> findAll() {
        return this.recipeRepository.findAll();
    }

    /**
     * Creates a {@link Recipe} entity based on the provided {@link RecipeDto}.
     *
     * <p>This method creates a new {@link Recipe} from the provided DTO, maps tags and ingredients
     * to their respective entities, and persists the new recipe in the database.
     *
     * @param recipeDto The {@link RecipeDto} containing the recipe data to be created.
     * @return The created {@link RecipeDto} with the assigned ID, or {@code null} if the recipe already exists.
     */
    public RecipeDto createRecipe(RecipeDto recipeDto) {
        // Map Recipe
        if (this.recipeRepository.findById((long) recipeDto.getId()).isPresent()) {
            return null;
        }
        Recipe recipe = new Recipe();
        recipe = this.saveRecipeMiddleware(recipe, recipeDto);

        // Save Recipe
        Recipe tmpRecipe = this.recipeRepository.save(recipe);
        return mapToDto(tmpRecipe);
    }

    /**
     * A helper method to map the fields of a {@link RecipeDto} to a {@link Recipe} entity.
     *
     * <p>This method sets the fields of a {@link Recipe} entity based on the provided {@link RecipeDto},
     * mapping tags and ingredients to their respective entities.
     *
     * @param recipe The {@link Recipe} entity to be populated.
     * @param recipeDto The {@link RecipeDto} containing the recipe data.
     * @return The populated {@link Recipe} entity.
     */
    public Recipe saveRecipeMiddleware(Recipe recipe, RecipeDto recipeDto) {
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setPersonAmount(recipeDto.getPersonAmount());
        recipe.setTimeEffort(recipeDto.getTimeEffort());
        recipe.setInstructions(recipeDto.getInstructions());
        recipe.setImageSrc(recipeDto.getImgSrc());

        Set<Tag> tags = new HashSet<>();
        Set<RecipeIngredient> recipeIngredients = new HashSet<>();
        // Map Tags
        if (recipeDto.getTags() != null) {
            tags = recipeDto.getTags().stream()
                    .map(tagName -> tagRepository.findByName(tagName)
                            .orElseGet(() -> {
                                Tag tag = new Tag();
                                tag.setName(tagName);
                                return tagRepository.save(tag);
                            }))
                    .collect(Collectors.toSet());
        }
        recipe.setTags(tags);

        // Map Ingredients
        if (recipeDto.getIngredients() != null) {
            recipeIngredients = recipeDto.getIngredients().stream()
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
        }
        recipe.setIngredients(recipeIngredients);

        return recipe;
    }

    /**
     * Updates a {@link Recipe} entity based on the provided {@link RecipeDto}.
     *
     * <p>This method updates the fields of an existing recipe by using the data from the provided DTO.
     * The updated recipe is then saved back into the database.
     *
     * @param recipeDto The {@link RecipeDto} containing the updated recipe data.
     * @return The updated {@link RecipeDto}, or {@code null} if the recipe with the specified ID does not exist.
     */
    public RecipeDto updateRecipeById(RecipeDto recipeDto) {
        Recipe recipe = this.recipeRepository.findById((long) recipeDto.getId()).orElse(null);
        if (recipe == null) {
            return null;
        }
        recipe = this.saveRecipeMiddleware(recipe, recipeDto);

        return this.mapToDto(recipe);
    }

    /**
     * Converts a {@link Recipe} entity to a {@link RecipeDto}.
     *
     * <p>This method maps the fields of a {@link Recipe} entity to a {@link RecipeDto},
     * including tags and ingredients, which are also mapped to their respective DTOs.
     *
     * @param recipe The {@link Recipe} entity to be converted.
     * @return A {@link RecipeDto} containing the data of the given recipe.
     */
    public RecipeDto mapToDto(Recipe recipe) {
        RecipeDto dto = new RecipeDto();
        dto.setId(recipe.getId());
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

    /**
     * Converts a {@link RecipeIngredient} entity to an {@link IngredientDto}.
     *
     * <p>This method maps the fields of a {@link RecipeIngredient} entity to an {@link IngredientDto}.
     *
     * @param recipeIngredient The {@link RecipeIngredient} entity to be converted.
     * @return An {@link IngredientDto} containing the data of the given recipe ingredient.
     */
    private IngredientDto mapIngredientToDto(RecipeIngredient recipeIngredient) {
        IngredientDto dto = new IngredientDto();
        dto.setName(recipeIngredient.getIngredient().getName());
        dto.setAmount(recipeIngredient.getAmount());
        dto.setUnit(recipeIngredient.getUnit());
        return dto;
    }
}
