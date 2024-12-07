package org.schmuh.quisine.controller;

import net.sourceforge.tess4j.TesseractException;
import org.schmuh.quisine.services.OCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ocr")
public class OCRController {

    @Autowired
    OCRService ocrService;

    @PostMapping("ocrTest")
    public ResponseEntity<String> ocrTest() {

        try{
        this.ocrService.GetTextFromPicture("C:/Uni/GruppenProjekt/q_backend/src/main/resources/images/test/rezept4.jpg");
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body("Sieht supi aus diggi");
    }

}
