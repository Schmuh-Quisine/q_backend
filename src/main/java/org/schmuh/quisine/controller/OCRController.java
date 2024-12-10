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

    /**
     * Endpoint for testing the OCR (Optical Character Recognition) service.
     *
     * <p>This method invokes the OCR service to extract text from a predefined image file
     * located at <code>C:/Uni/GruppenProjekt/q_backend/src/main/resources/images/test/rezept4.jpg</code>.
     * If the OCR process is successful, it returns a positive response message.
     * In case of an error, a {@link RuntimeException} is thrown.
     *
     * @return a {@link ResponseEntity} containing a success message if the operation completes successfully
     */
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
