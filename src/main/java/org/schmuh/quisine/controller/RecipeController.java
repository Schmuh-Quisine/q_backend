package org.schmuh.quisine.controller;

import org.json.JSONObject;
import org.schmuh.quisine.entity.Recipe;
import org.schmuh.quisine.entity.Tag;
import org.schmuh.quisine.services.IngredientService;
import org.schmuh.quisine.services.OCRService;
import org.schmuh.quisine.services.RecipeService;
import org.schmuh.quisine.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/")
public class RecipeController {

    @Autowired
    RecipeService recipeService;
    @Autowired
    TagService tagService;
    @Autowired
    IngredientService ingredientService;

    @PostMapping("recipe")
    public ResponseEntity<Void> postRecipe(@RequestBody JSONObject recipeJson) {

        // retrieve native Recipe Information from json object
        Recipe tmpRecipe = new Recipe();
        tmpRecipe.setTitle(recipeJson.getString("Title"));
        tmpRecipe.setDescription(recipeJson.getString("Description"));
        tmpRecipe.setPersonAmount(recipeJson.getInt("PersonAmount"));
        tmpRecipe.setInstructions(recipeJson.getString("Instructions"));
        tmpRecipe.setImageSrc(recipeJson.getString("ImageSrc"));

        this.ingredientService.saveFromJson(recipeJson.getJSONArray("Ingredients"));
        // TODO Check if this is really the correct way, maybe there is an automated way in OCR.
        // TODO to the same if not possible for recipeIngredients
        return ResponseEntity.ok().build();
    }

    @GetMapping("recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable("id") long id) {

        Recipe tmpRecipe = this.recipeService.getRecipeById(id);
        return ResponseEntity.ok().body(tmpRecipe);
    }

    @PutMapping("recipe/{id}")
    public ResponseEntity<String> putRecipe(@PathVariable("id") long id, @RequestBody Recipe recipe) {
        this.recipeService.updateRecipeById(id, recipe);
        return ResponseEntity.ok().body("Recipe Saved");
    }

    @GetMapping("recipe/all")
    public ResponseEntity<List<Recipe>> getAllRecipe() {

        List<Recipe> recipesList = this.recipeService.findAll();
        return ResponseEntity.ok().body(recipesList);
    }

    @GetMapping("tags/all")
    public ResponseEntity<List<Tag>> getAllTag() {

        List<Tag> tagList = this.tagService.findAll();
        return ResponseEntity.ok().body(tagList);
    }

    @PostMapping("tag")
    public ResponseEntity<Void> postRecipe(@RequestBody Tag tag) {
        this.tagService.save(tag);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            // Validate the file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
                return ResponseEntity.badRequest().body("File is not an image");
            }

            // Simulate saving the file (you can save it to the file system or a database)
            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            String fileType = file.getContentType();

            // Log or process the file as needed
            System.out.println("Received file: " + fileName + " (Type: " + fileType + ", Size: " + fileSize + " bytes)");

            return ResponseEntity.ok("Image uploaded successfully: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while uploading the image");
        }
    }


}
