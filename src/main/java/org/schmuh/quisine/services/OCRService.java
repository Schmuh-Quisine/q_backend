package org.schmuh.quisine.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@NoArgsConstructor
@AllArgsConstructor
public class OCRService {
    @Value("${ocrPath}")
    private String ocrPath;

    public String GetTextFromPicture(){
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(ocrPath);
        return "";
    }
}
