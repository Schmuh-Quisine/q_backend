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

@Service
@NoArgsConstructor
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    public List<Ingredient> findAll() {
        return this.ingredientRepository.findAll();
    }

    public void save(Ingredient ingredient) {
        this.ingredientRepository.save(ingredient);
    }

    public void saveAll(List<Ingredient> ingredients) {
        this.ingredientRepository.saveAll(ingredients);
    }

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