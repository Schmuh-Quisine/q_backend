package org.schmuh.quisine.services;

import lombok.NoArgsConstructor;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);

        CLAHE clahe = Imgproc.createCLAHE();
        clahe.setClipLimit(3.5);
        clahe.setTilesGridSize(new Size(8,8));
        clahe.apply(image, image);

        Core.normalize(image, image, 0, 255, Core.NORM_MINMAX);

        Photo.fastNlMeansDenoising(image, image, 20);
        writeImage(image, "preProcessedImage.jpg");

        Mat thresh = new Mat();
        Imgproc.adaptiveThreshold(image, thresh, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 15, 10);
        writeImage(thresh, "aThresholdImage.jpg");

        var contours = GetRowContours(thresh);
        DrawRectContours(image, contours);
        writeImage(thresh, "contours.jpg");
    }

    private ArrayList<MatOfPoint> GetRowContours(Mat image){
        Mat dilte = image.clone();
        var kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(20,3));
        Imgproc.dilate(image, dilte, kernel, new Point(-1,-1), 2);
        writeImage(dilte, "dilateImage.jpg");

        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dilte, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        System.out.println(contours.size());

        Mat rectangles = new Mat();
        rectangles = image.clone();
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            Imgproc.rectangle(rectangles, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
            double area = Imgproc.contourArea(contour);
            if (rect.width > rect.height * 5 || rect.height > rect.width * 2 ||area < 500){
                continue;
            }
            Imgproc.drawContours(image, contours, contours.indexOf(contour), new Scalar(0, 0, 0), -2);
        }
        writeImage(rectangles, "rectanglesImage.jpg");
        return contours;
    }

    private void DrawRectContours(Mat image, ArrayList<MatOfPoint> contours){
        Mat rectangles = new Mat();
        rectangles = image.clone();
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            Imgproc.rectangle(rectangles, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
            double area = Imgproc.contourArea(contour);
        }
        writeImage(rectangles, "rectanglesImage.jpg");
    }

    private void writeImage(Mat image, String imageName)
    {
        newImage = "/" + imageName;
        Imgcodecs.imwrite(testFolder + folderName + newImage, image);
    }
}
