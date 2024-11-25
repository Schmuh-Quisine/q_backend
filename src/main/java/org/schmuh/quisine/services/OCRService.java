package org.schmuh.quisine.services;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.PrintWriter;


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
        //tesseract.setTessVariable("tessedit_char_whitelist", "");
    }

    public void GetTextFromPicture() throws TesseractException {
        var file = this.imageService.GetImage("rezept1.jpg");
        var teststring = tesseract.doOCR(file);

        //temp
        try{
        var printwriter = new PrintWriter(file.getParent() + "/OCRText.txt");
        printwriter.println(teststring);
        printwriter.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        //temp

        System.out.println("-------------------Rezept anfang---------------------------");
        System.out.println(teststring);
        System.out.println("-------------------Rezept ende---------------------------");

    }
}
