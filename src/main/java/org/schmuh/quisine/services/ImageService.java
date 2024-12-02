package org.schmuh.quisine.services;

import lombok.NoArgsConstructor;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.io.File;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

@Service
@NoArgsConstructor
public class ImageService {


    @Value("${uploadDir}")
    String uploadDir;
    @Value("${testImageFolderPath}")
    private String testFolder;
    private String folderName = "Folder2" + new SimpleDateFormat("HH_mm_ss").format(new Date());
    public String folderPath;

    public Pair<BufferedImage, ArrayList<Rectangle>> GetImage (String filepath){
        nu.pattern.OpenCV.loadLocally();
        tempCreateImageFolder();

        Mat startImage = Imgcodecs.imread(filepath);
        Imgproc.resize(startImage, startImage, new Size(2490, 3510), 2, 2, Imgproc.INTER_CUBIC);

        Mat preProcessedImage = imagePreprocessing(startImage.clone());

        var contours = getRowContours(preProcessedImage);
        filterContours(contours, preProcessedImage);
        contours = getRowContours(preProcessedImage);
        //contours = getWordContours(preProcessedImage);
        var rects = GetRectangles(startImage, contours);
        SortRectangles(rects);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2.5, 2));
        Imgproc.erode(preProcessedImage, preProcessedImage, kernel);
        writeImage(Pair.of(preProcessedImage, "erodeImage.jpg"));

        return Pair.of(matToBufferdImageConverter(preProcessedImage), rectToRectangleConverter(rects));
    }

    private Mat imagePreprocessing(Mat image){
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);

        CLAHE clahe = Imgproc.createCLAHE();
        clahe.setClipLimit(2.0); //evtl 3.5
        clahe.setTilesGridSize(new Size(16,16)); // evtl 8,8
        clahe.apply(image, image);

        Core.normalize(image, image, 0, 255, Core.NORM_MINMAX);
        writeImage(Pair.of(image, "normalizeImage.jpg"));

        Photo.fastNlMeansDenoising(image, image, 20);
        writeImage(Pair.of(image, "preProcessedImage.jpg"));

        Imgproc.adaptiveThreshold(image, image, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 21, 5); // c =2
        writeImage(Pair.of(image, "ThresholdImage.jpg"));

        return image;
    }

    private ArrayList<MatOfPoint> getRowContours(Mat image){
        Mat dilate = image.clone();
        var kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(25,3)); //if we want to read the numbers (e.g: "1." we need to adapt the width to 25, if not set to 10
        Imgproc.dilate(image, dilate, kernel, new Point(-1,-1), 2);
        writeImage(Pair.of(dilate, "dilteRowImage.jpg"));

        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dilate, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        System.out.println(contours.size()); // temp

        return contours;
    }

    private ArrayList<MatOfPoint> getWordContours(Mat image){
        Mat dilate = image.clone();
        var kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(6,3));
        Imgproc.dilate(image, dilate, kernel, new Point(-1,-1), 2);
        writeImage(Pair.of(dilate, "dilteWordImage.jpg"));

        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dilate, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        System.out.println(contours.size()); // temp

        return contours;
    }

    private void filterContours(ArrayList<MatOfPoint> contours, Mat image){
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            double area = Imgproc.contourArea(contour);
            if (rect.width > rect.height * 5 && area > 500){
                continue;
            }
            Imgproc.drawContours(image, contours, contours.indexOf(contour), new Scalar(0, 0, 0), -2);
        }
        writeImage(Pair.of(image, "filledContours.jpg"));
    }

    //temp
    private ArrayList<Rect> GetRectangles(Mat image, ArrayList<MatOfPoint> contours){
        ArrayList<Rect> rects = new ArrayList<>();
        Mat rectangles = image.clone();
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            rects.add(rect);
            Imgproc.rectangle(rectangles, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2); // temp
        }
        writeImage(Pair.of(rectangles, "rectangles.jpg")); // temp
        return rects;
    }

    private void SortRectangles(ArrayList<Rect> rects){
        rects.sort(new Comparator<Rect>() {
            @Override
            public int compare(Rect r1, Rect r2) {
                if (Math.abs(r1.y - r2.y) <= 10) {
                    return Integer.compare(r1.x, r2.x);
                }
                return Integer.compare(r1.y, r2.y);
            }
        });
    }

    private void writeImage(Pair<Mat, String> image)
    {
        Imgcodecs.imwrite(testFolder + folderName + "/" + image.getSecond(), image.getFirst());
    }

    private void tempCreateImageFolder(){
        folderPath = testFolder + folderName;
        new File(folderPath).mkdir();
    }

    private BufferedImage matToBufferdImageConverter(Mat mat){
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_BYTE_GRAY);
        mat.get(0,0,((DataBufferByte) image.getRaster().getDataBuffer()).getData());
        return image;
    }

    private Mat bufferedImageToMatConverter(BufferedImage image){
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    private ArrayList<Rectangle> rectToRectangleConverter(ArrayList<Rect> rects){
        ArrayList<Rectangle> rectangles = new ArrayList<>();
        for (Rect rect : rects) {
            rectangles.add(new Rectangle(rect.x, rect.y, rect.width, rect.height));
        }
        return rectangles;
    }

    public String saveImage(MultipartFile file){
        String fileName = file.getOriginalFilename();

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            if (fileName != null) {
                Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }else{
                return "";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return uploadPath.resolve(fileName).toAbsolutePath().toString();
    }
}
