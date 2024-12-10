package org.schmuh.quisine.controller;

import net.sourceforge.tess4j.TesseractException;
import org.schmuh.quisine.dto.RecipeDto;
import org.schmuh.quisine.entity.Recipe;
import org.schmuh.quisine.entity.Tag;
import org.schmuh.quisine.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/")
public class RecipeController {

    @Autowired
    RecipeService recipeService;
    @Autowired
    ImageService imageService;
    @Autowired
    OCRService ocrService;


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

    @PostMapping("/upload")
    public ResponseEntity<RecipeDto> handleFileUpload(@RequestParam("image") MultipartFile file) {
        try {

            RecipeDto returnDto = new RecipeDto();
            String filePath = this.imageService.saveImage(file);
            returnDto = this.ocrService.GetTextFromPicture(filePath);
            return ResponseEntity.ok().body(returnDto);
        } catch (TesseractException e) {
            return ResponseEntity.status(500).body(new RecipeDto());
        }
    }


}
