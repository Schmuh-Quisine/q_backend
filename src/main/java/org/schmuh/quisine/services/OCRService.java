package org.schmuh.quisine.services;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.util.Dictionary;
import java.util.Hashtable;
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

    private Tesseract tesseract;

    @PostConstruct
    private void InitOCRService(){
        tesseract = new Tesseract();
        tesseract.setDatapath(ocrPath);
        tesseract.setLanguage("deu");
    }

    public void GetTextFromPicture() throws TesseractException {
        var file = this.imageService.GetImage("rezept1.jpg");
        var teststring = tesseract.doOCR(file);

        System.out.println("-------------------Rezept anfang---------------------------");
        System.out.println(teststring);
        System.out.println("-------------------Rezept ende---------------------------");

        // var test = getIngredients(teststring)
    }

    private Dictionary<String, String> getIngredients(String ocrText){
        Pattern pattern = Pattern.compile("(\\d+\\s?(g|ml|TL|EL|Prise)?)?\\s?([\\w\\s]+)");
        Matcher matcher = pattern.matcher(ocrText);

        Dictionary<String, String> ingredients = new Hashtable<>();

        while(matcher.find()){
            String amount = matcher.group(1) != null ? matcher.group(1) : "";
            String ingredient = matcher.group(3);
            ingredients.put(ingredient, amount);
        }
        System.out.println("-------------------Ingredients anfang---------------------------");
        System.out.println(ingredients);
        System.out.println("-------------------Ingredients ende---------------------------");
        return ingredients;
    }
}
