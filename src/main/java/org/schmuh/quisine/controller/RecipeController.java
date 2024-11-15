package org.schmuh.quisine.controller;

import org.schmuh.quisine.dto.Recipe;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.IllegalFormatPrecisionException;

@RestController
@RequestMapping("/api/v1/")
public class RecipeController {


@PostMapping("recipe")
public ResponseEntity<Void> postRecipe(@RequestBody Recipe recipe){

    System.out.println("Received Recipe!: " + recipe.name);

    return ResponseEntity.ok().build();
}

@GetMapping("recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable("id") int id ){

    Recipe tmpRecipe = new Recipe();
    System.out.println("Sending Recipe with id: " + tmpRecipe.id);
    return ResponseEntity.ok().body(tmpRecipe);
}
;}
