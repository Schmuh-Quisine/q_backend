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

    /**
     * Endpoint for creating a new recipe.
     *
     * <p>This method accepts a recipe object as input, processes it through the recipe service,
     * and returns the created recipe along with an appropriate HTTP status code.
     *
     * <ul>
     *     <li>Returns <code>201 Created</code> if the recipe is successfully created.</li>
     *     <li>Returns <code>409 Conflict</code> if the creation process fails (e.g., due to a conflict).</li>
     * </ul>
     *
     * @param recipeDto the {@link RecipeDto} object containing the details of the recipe to be created
     * @return a {@link ResponseEntity} containing the saved recipe and the corresponding HTTP status code
     */
    @PostMapping("recipes")
    public ResponseEntity<RecipeDto> postRecipe(@RequestBody RecipeDto recipeDto) {

        RecipeDto savedRecipe = recipeService.createRecipe(recipeDto);
        if (savedRecipe != null) {
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
    }
    /**
     * Endpoint for retrieving a recipe by its ID.
     *
     * <p>This method fetches a recipe using the provided ID and returns it in the response body.
     * The recipe is retrieved through the recipe service.
     *
     * @param id the ID of the recipe to be retrieved
     * @return a {@link ResponseEntity} containing the {@link RecipeDto} with the requested recipe details
     *         and an HTTP status of <code>200 OK</code>
     */
    @GetMapping("recipes/{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable("id") long id) {

        RecipeDto tmpRecipeDto = this.recipeService.getRecipeById(id);
        return ResponseEntity.ok().body(tmpRecipeDto);
    }
    /**
     * Endpoint for updating an existing recipe by its ID.
     *
     * <p>This method updates the details of a recipe specified by its ID using the data provided
     * in the request body. If the recipe is successfully updated, the updated recipe is returned
     * with an HTTP status of <code>200 OK</code>. If the recipe with the specified ID is not found,
     * it returns an HTTP status of <code>404 Not Found</code>.
     *
     * @param id the ID of the recipe to be updated
     * @param recipeDto the {@link RecipeDto} object containing the updated recipe details
     * @return a {@link ResponseEntity} containing the updated {@link RecipeDto} and the corresponding HTTP status
     */
    @PutMapping("recipes/{id}")
    public ResponseEntity<RecipeDto> putRecipe(@PathVariable("id") long id, @RequestBody RecipeDto recipeDto) {
        RecipeDto recipeDtoUpdated = this.recipeService.updateRecipeById(recipeDto);
        if (recipeDtoUpdated == null){
                return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(recipeDtoUpdated);
    }
    /**
     * Endpoint for retrieving all recipes.
     *
     * <p>This method fetches all recipes from the database, maps them to {@link RecipeDto} objects,
     * and returns them as a list in the response body with an HTTP status of <code>200 OK</code>.
     *
     * @return a {@link ResponseEntity} containing a list of {@link RecipeDto} objects representing all recipes
     */

    @GetMapping("recipes")
    public ResponseEntity<List<RecipeDto>> getAllRecipe() {

        List<Recipe> recipesList = this.recipeService.findAll();
        List<RecipeDto> recipeDtoList = new ArrayList<>();
        for (Recipe recipe : recipesList) {
            recipeDtoList.add(this.recipeService.mapToDto(recipe));
        }
        return ResponseEntity.ok().body(recipeDtoList);
    }
    /**
     * Endpoint for deleting a recipe by its ID.
     *
     * <p>This method deletes the recipe specified by its ID. If the deletion is successful,
     * it returns a success message with an HTTP status of <code>200 OK</code>.
     *
     * @param id the ID of the recipe to be deleted
     * @return a {@link ResponseEntity} containing a success message and the corresponding HTTP status
     */
    @DeleteMapping("recipes/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id") long id) {
        this.recipeService.deleteRecipe(id);
        return ResponseEntity.ok().body("Recipe Deleted");
    }

    /**
     * Endpoint for handling file uploads and processing the uploaded image for OCR.
     *
     * <p>This method accepts an image file, saves it to the server, and processes it using OCR
     * to extract recipe details. The extracted details are returned as a {@link RecipeDto}.
     * If an error occurs during the OCR process, a response with an HTTP status of <code>500 Internal Server Error</code>
     * is returned along with an empty {@link RecipeDto}.
     *
     * @param file the uploaded image file to be processed
     * @return a {@link ResponseEntity} containing the extracted {@link RecipeDto} on success,
     *         or an empty {@link RecipeDto} with an error status in case of failure
     */
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
