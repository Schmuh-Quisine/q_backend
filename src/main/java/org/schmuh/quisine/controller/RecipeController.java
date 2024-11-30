package org.schmuh.quisine.controller;

import org.schmuh.quisine.dto.RecipeDto;
import org.schmuh.quisine.entity.Recipe;
import org.schmuh.quisine.entity.Tag;
import org.schmuh.quisine.services.IngredientService;
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

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/v1/")
public class RecipeController {

    @Autowired
    RecipeService recipeService;


    @PostMapping("recipes")
    public ResponseEntity<RecipeDto> postRecipe(@RequestBody RecipeDto recipeDto) {

        RecipeDto savedRecipe = recipeService.createRecipe(recipeDto);
        if (savedRecipe != null) {
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    }

    @GetMapping("recipes/{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable("id") long id) {

        RecipeDto tmpRecipeDto = this.recipeService.getRecipeById(id);
        return ResponseEntity.ok().body(tmpRecipeDto);
    }

    @PutMapping("recipes/{id}")
    public ResponseEntity<RecipeDto> putRecipe(@PathVariable("id") long id, @RequestBody RecipeDto recipeDto) {
        RecipeDto recipeDtoUpdated = this.recipeService.updateRecipeById(recipeDto);
        if (recipeDtoUpdated == null){
                return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(recipeDtoUpdated);
    }

    @GetMapping("recipes")
    public ResponseEntity<List<RecipeDto>> getAllRecipe() {

        List<Recipe> recipesList = this.recipeService.findAll();
        List<RecipeDto> recipeDtoList = new ArrayList<>();
        for (Recipe recipe : recipesList) {
            recipeDtoList.add(this.recipeService.mapToDto(recipe));
        }
        return ResponseEntity.ok().body(recipeDtoList);
    }

    @DeleteMapping("recipes/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id") long id) {
        this.recipeService.deleteRecipe(id);
        return ResponseEntity.ok().body("Recipe Deleted");
    }
    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
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
