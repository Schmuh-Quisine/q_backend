package org.schmuh.quisine.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.schmuh.quisine.dto.IngredientDto;
import org.schmuh.quisine.dto.RecipeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@AllArgsConstructor
@NoArgsConstructor
public class OCRService {
    @Value("${ocrPath}")
    private String ocrPath;

    @Autowired
    private ImageService imageService;
    @Autowired
    private RecipeService recipeService;

    private Tesseract tesseract;

    @PostConstruct
    private void InitOCRService(){
        tesseract = new Tesseract();
        tesseract.setDatapath(ocrPath);
        tesseract.setLanguage("deu");
        tesseract.setTessVariable("user_defined_dpi", "300");
        //tesseract.setTessVariable("tessedit_char_whitelist", "");
    }

    public RecipeDto GetTextFromPicture(String filepath) throws TesseractException {
        var file = this.imageService.GetImage(filepath);
        var ocrText = new String();

        for (Rectangle rect : file.getSecond()){
            ocrText += tesseract.doOCR(file.getFirst(), rect);
        }

        /*
        try{
        var printwriter = new PrintWriter(this.imageService.folderPath + "/OCRText.txt");
        printwriter.println(teststring);
        printwriter.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        */

        // Replace wrong character
        ocrText = ocrText.replace("TI.", "Tl.");
        ocrText = ocrText.replaceAll("[ \t]+", " ").trim();

        System.out.println("-------------------Rezept anfang---------------------------");
        System.out.println(ocrText);
        System.out.println("-------------------Rezept ende---------------------------");

        // Split ocrText to two Strings => ingredients and description
        String ingredients = "";
        String description = "";
        Pattern IngredientPattern = Pattern.compile("(?<=Zutaten:)(.*?)(?=Zubereitung:)", Pattern.DOTALL);
        Pattern DescriptionPattern = Pattern.compile("(?<=Zubereitung:)(.*)", Pattern.DOTALL);
        Matcher matcher = IngredientPattern.matcher(ocrText);
        if (matcher.find()){
            ingredients = matcher.group(1).trim();
        }
        matcher = DescriptionPattern.matcher(ocrText);
        if (matcher.find()){
            description = matcher.group(1).trim();
        }

        // Get all Ingredients from ingredients string
        ArrayList<IngredientDto> ingredientsList = GetIngredients(ingredients);

        // Get Title
        String[] tempSplit = ingredients.split("\n", 2);
        String title = tempSplit[0];

        RecipeDto tmpRecipeDto = new RecipeDto();
        tmpRecipeDto.setTitle(title);
        tmpRecipeDto.setDescription(description);
        tmpRecipeDto.setIngredients(ingredientsList);

        //...
        return this.recipeService.createRecipe(tmpRecipeDto);
    }

    private ArrayList<IngredientDto> GetIngredients(String ocrString){
        ArrayList<IngredientDto> ingredients = new ArrayList<>();

        String[] lines = ocrString.split("\\r?\\n");
        Pattern pattern = Pattern.compile("(\\d+)\\s*((?:\\S+\\s+)?Prise|ml|g|kg|mg|l|Tl\\.|El\\.)?\\s*([A-Za-zÄäÖöÜüß]+(?:\\s+[A-Za-zÄäÖöÜüß]+)*)"); //(\d+)\s+((?:\S+\s+)?Prise|ml|g|kg|mg|l|Tl\.|El\.)\s+(.*) // group 3 should be adapted => overengineered

        for (String line : lines){
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()){
                IngredientDto ingredient = new IngredientDto();
                ingredient.setAmount(matcher.group(1) != null ? Double.parseDouble(matcher.group(1).trim()) : 0);
                ingredient.setUnit(matcher.group(2) != null ? matcher.group(2).trim() : "");
                ingredient.setName(matcher.group(3)!= null ? matcher.group(3).trim() : "");
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }
}
