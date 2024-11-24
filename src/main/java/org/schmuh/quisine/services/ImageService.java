package org.schmuh.quisine.services;

import lombok.NoArgsConstructor;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@NoArgsConstructor
public class ImageService {
    @Value("${testImageFolderPath}")
    private String testFolder;
    private String folderName = "Folder" + new SimpleDateFormat("HH_mm_ss").format(new Date());
    private String newImage;

    public File GetImage (String imageName){
        AdaptImage(imageName);
        return new File(testFolder + folderName + newImage);
    }

    /**
     * @param imageName
     * Improve image quality for better ocr result
     */
    private void AdaptImage(String imageName){
        nu.pattern.OpenCV.loadLocally();
        //Temp
        new File(testFolder + folderName).mkdir();

        Mat image = Imgcodecs.imread(testFolder + imageName);

        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        // writeImage(grayImage, "grayImage.jpg");

        CLAHE clahe = Imgproc.createCLAHE();
        clahe.setClipLimit(3.5);
        clahe.setTilesGridSize(new Size(8,8));
        clahe.apply(grayImage, grayImage);
        // writeImage(grayImage, "claheImage.jpg");

        Core.normalize(grayImage, grayImage, 0, 255, Core.NORM_MINMAX);
        // writeImage(grayImage, "normalizeImage.jpg");

        Photo.fastNlMeansDenoising(grayImage, grayImage, 20);
        // writeImage(grayImage, "NLImage.jpg");

        Imgproc.adaptiveThreshold(grayImage, grayImage, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 2);
        writeImage(grayImage, "aThresholdImage.jpg");

    }

    private void writeImage(Mat image, String imageName)
    {
        newImage = "/" + imageName;
        Imgcodecs.imwrite(testFolder + folderName + newImage, image);
    }
}
