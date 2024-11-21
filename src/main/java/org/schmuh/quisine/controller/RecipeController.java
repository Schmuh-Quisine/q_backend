package org.schmuh.quisine.controller;

import org.json.JSONObject;
import org.schmuh.quisine.dto.RecipeDto;
import org.schmuh.quisine.entity.Recipe;
import org.schmuh.quisine.entity.Tag;
import org.schmuh.quisine.services.IngredientService;
import org.schmuh.quisine.services.OCRService;
import org.schmuh.quisine.services.RecipeService;
import org.schmuh.quisine.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    public ResponseEntity<Recipe> postRecipe(@RequestBody RecipeDto recipeDto) {

        Recipe savedRecipe = recipeService.saveRecipe(recipeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
    }

    @GetMapping("recipe/{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable("id") long id) {

        RecipeDto tmpRecipeDto = this.recipeService.getRecipeById(id);
        return ResponseEntity.ok().body(tmpRecipeDto);
    }

    @PutMapping("recipe/{id}")
    public ResponseEntity<String> putRecipe(@PathVariable("id") long id, @RequestBody Recipe recipe) {
        this.recipeService.updateRecipeById(id, recipe);
        return ResponseEntity.ok().body("Recipe Saved");
    }

    @GetMapping("recipe/all")
    public ResponseEntity<List<RecipeDto>> getAllRecipe() {

        List<Recipe> recipesList = this.recipeService.findAll();
        List<RecipeDto> recipeDtoList = new ArrayList<>();
        for (Recipe recipe : recipesList) {
            recipeDtoList.add(this.recipeService.mapToDto(recipe));
        }
        return ResponseEntity.ok().body(recipeDtoList);
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
