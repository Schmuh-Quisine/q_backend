package org.schmuh.quisine.services;

import lombok.NoArgsConstructor;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
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

    private String newImage = "equalizedImage" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".jpg";

    public File GetImage (String imageName){
        AdaptImage(imageName);
        return new File(testFolder + newImage);
    }

    /**
     * @param imageName
     * Improve image quality for better ocr result
     */
    private void AdaptImage(String imageName){
        nu.pattern.OpenCV.loadLocally();

        Mat image = Imgcodecs.imread(testFolder + imageName);

        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        Imgproc.GaussianBlur(grayImage, grayImage, new Size(5, 5), 0);

        Mat equalizedImage = new Mat();
        Imgproc.equalizeHist(grayImage, equalizedImage);
        /*
        Mat binaryImage = new Mat();
        Imgproc.threshold(equalizedImage, binaryImage, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        */
        Imgcodecs.imwrite(testFolder + newImage, equalizedImage);
    }
}
