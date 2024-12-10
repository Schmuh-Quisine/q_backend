package org.schmuh.quisine.services;


import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.schmuh.quisine.entity.Ingredient;
import org.schmuh.quisine.entity.Tag;
import org.schmuh.quisine.repository.IngredientRepository;
import org.schmuh.quisine.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing {@link Ingredient} entities.
 *
 * <p>This service provides methods for retrieving, saving, and processing ingredient data.
 * It interacts with the {@link IngredientRepository} to handle the persistence of ingredients and offers functionality
 * to save ingredients both individually and in bulk, including the ability to parse and save ingredients from a JSON array.
 */
@Service
@NoArgsConstructor
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    /**
     * Retrieves a list of all {@link Ingredient} entities from the database.
     *
     * <p>This method uses the {@link IngredientRepository} to fetch all ingredient records from the database.
     * It returns a list containing all the {@link Ingredient} entities.
     *
     * @return A list of all {@link Ingredient} entities.
     */
    public List<Ingredient> findAll() {
        return this.ingredientRepository.findAll();
    }

    /**
     * Saves a given {@link Ingredient} entity to the database.
     *
     * <p>This method uses the {@link IngredientRepository} to save the provided {@link Ingredient} entity.
     * If the ingredient already exists, it will be updated; otherwise, a new ingredient will be created.
     *
     * @param ingredient The {@link Ingredient} entity to be saved.
     */
    public void save(Ingredient ingredient) {
        this.ingredientRepository.save(ingredient);
    }

    /**
     * Saves a list of {@link Ingredient} entities to the database.
     *
     * <p>This method uses the {@link IngredientRepository} to save all the provided {@link Ingredient} entities.
     * If any ingredients already exist, they will be updated; otherwise, new ingredients will be created.
     *
     * @param ingredients The list of {@link Ingredient} entities to be saved.
     */
    public void saveAll(List<Ingredient> ingredients) {
        this.ingredientRepository.saveAll(ingredients);
    }

    /**
     * Saves a list of {@link Ingredient} entities from a JSON array to the database.
     *
     * <p>This method iterates through a {@link JSONArray}, extracts the ingredient data, and creates a list
     * of {@link Ingredient} objects. The extracted ingredients are then saved using the {@link save} method.
     * Each ingredient is created based on the "Name" field from the JSON objects.
     *
     * @param jsonArray The {@link JSONArray} containing ingredient data to be saved.
     *                  Each element of the array should be a {@link JSONObject} with a "Name" field.
     */
    public void saveFromJson(JSONArray jsonArray) {

        List<Ingredient> ingredients = new ArrayList<>();
        // Iterating through JSONArray with foreach
        for (Object obj : jsonArray) {
            if (obj instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) obj;
                String name = jsonObject.getString("Name");
                Ingredient tmpIngredient = new Ingredient();
                tmpIngredient.setName(name);
                ingredients.add(tmpIngredient);
            }
        }
        for (Ingredient ingredient : ingredients) {
            this.save(ingredient);
        }
    }
}