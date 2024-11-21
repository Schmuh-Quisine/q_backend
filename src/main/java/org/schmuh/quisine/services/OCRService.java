package org.schmuh.quisine.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;


@Service
@NoArgsConstructor
@AllArgsConstructor
public class OCRService {
    @Value("${ocrPath}")
    private String ocrPath;

    @Value("${testImageFolderPath}")
    private String testFolder;

    public String GetTextFromPicture(){
//        Tesseract tesseract = new Tesseract();
//        tesseract.setDatapath(ocrPath);
        System.out.print("OCR_TEST Works");
        return "";
    }
}
