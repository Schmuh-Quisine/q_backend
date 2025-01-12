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
    private String folderName = "Folder3" + new SimpleDateFormat("HH_mm_ss").format(new Date());
    public String folderPath;

    private ArrayList<Pair<Mat, String>> imagesToSave = new ArrayList<>();

    /**
     * Reads an image from the specified file path, applies pre-processing techniques,
     * detects contours in the image, and returns the processed image along with the detected rectangles.
     *
     * @param filepath The path to the image file.
     * @return A {@link Pair} containing the pre-processed image as a {@link BufferedImage} and a list of {@link Rectangle} objects.
     */
    public Pair<BufferedImage, ArrayList<Rectangle>> GetImage (String filepath){
        nu.pattern.OpenCV.loadLocally();

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
        imagesToSave.add(Pair.of(preProcessedImage.clone(), "erodeImage.jpg"));
        // writeImage(imagesToSave);
        return Pair.of(matToBufferdImageConverter(preProcessedImage), rectToRectangleConverter(rects));
    }

    /**
     * Pre-processes the provided image by applying color conversion, CLAHE (Contrast Limited Adaptive Histogram Equalization),
     * denoising, and adaptive thresholding.
     *
     * @param image The image to be pre-processed.
     * @return The pre-processed image as a {@link Mat} object.
     */
    private Mat imagePreprocessing(Mat image){
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);

        CLAHE clahe = Imgproc.createCLAHE();
        clahe.setClipLimit(2.0); //evtl 3.5
        clahe.setTilesGridSize(new Size(16,16)); // evtl 8,8
        clahe.apply(image, image);

        Core.normalize(image, image, 0, 255, Core.NORM_MINMAX);
        imagesToSave.add(Pair.of(image.clone(), "normalizeImage.jpg"));

        Photo.fastNlMeansDenoising(image, image, 20);
        imagesToSave.add(Pair.of(image.clone(), "preProcessedImage.jpg"));

        Imgproc.adaptiveThreshold(image, image, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 21, 2); // c =5
        imagesToSave.add(Pair.of(image.clone(), "ThresholdImage.jpg"));

        return image;
    }

    /**
     * Finds and returns the row-based contours in the provided image.
     *
     * @param image The image for which row-based contours need to be detected.
     * @return A list of {@link MatOfPoint} representing the contours.
     */
    private ArrayList<MatOfPoint> getRowContours(Mat image){
        Mat dilate = image.clone();
        var kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(25,3)); //if we want to read the numbers (e.g: "1." we need to adapt the width to 25, if not set to 10
        Imgproc.dilate(image, dilate, kernel, new Point(-1,-1), 2);
        imagesToSave.add(Pair.of(dilate.clone(), "dilteRowImage.jpg"));

        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dilate, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        System.out.println(contours.size()); // temp

        return contours;
    }

    /**
     * Finds and returns the word-based contours in the provided image.
     *
     * @param image The image for which word-based contours need to be detected.
     * @return A list of {@link MatOfPoint} representing the contours.
     */
    private ArrayList<MatOfPoint> getWordContours(Mat image){
        Mat dilate = image.clone();
        var kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(6,3));
        Imgproc.dilate(image, dilate, kernel, new Point(-1,-1), 2);
        imagesToSave.add(Pair.of(dilate.clone(), "dilteWordImage.jpg"));

        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dilate, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        System.out.println(contours.size()); // temp

        return contours;
    }

    /**
     * Filters out unwanted contours from the provided list of contours based on size and aspect ratio.
     *
     * @param contours The list of contours to be filtered.
     * @param image The image on which contours are drawn after filtering.
     */
    private void filterContours(ArrayList<MatOfPoint> contours, Mat image){
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            double area = Imgproc.contourArea(contour);
            if (rect.width > rect.height * 5 && area > 500){
                continue;
            }
            Imgproc.drawContours(image, contours, contours.indexOf(contour), new Scalar(0, 0, 0), -2);
        }
        imagesToSave.add(Pair.of(image.clone(), "filledContours.jpg"));
    }

    /**
     * Converts a list of contours into rectangles and draws them on the image.
     *
     * @param image The image on which rectangles are drawn.
     * @param contours The list of contours to be converted into rectangles.
     * @return A list of {@link Rect} representing the bounding rectangles of the contours.
     */
    private ArrayList<Rect> GetRectangles(Mat image, ArrayList<MatOfPoint> contours){
        ArrayList<Rect> rects = new ArrayList<>();
        Mat rectangles = image.clone();
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            rects.add(rect);
            Imgproc.rectangle(rectangles, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
        }
        imagesToSave.add(Pair.of(rectangles.clone(), "rectangles.jpg"));
        return rects;
    }

    /**
     * Sorts a list of rectangles by their vertical position (y-coordinate), and within each row, sorts by horizontal position (x-coordinate).
     *
     * @param rects The list of rectangles to be sorted.
     */
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

    /**
     * Writes the processed images to disk, creating a folder in the specified test folder.
     *
     * @param imagesToSave A list of {@link Pair} objects containing the image matrix and the corresponding file name.
     */
    private void writeImage(ArrayList<Pair<Mat, String>> imagesToSave)
    {
        createImageFolder();
        for (Pair<Mat, String> image : imagesToSave){
            Imgcodecs.imwrite(testFolder + folderName + "/" + image.getSecond(), image.getFirst());
        }
    }

    /**
     * Creates a new folder for saving images.
     */
    private void createImageFolder(){
        folderPath = testFolder + folderName;
        new File(folderPath).mkdir();
    }

    /**
     * Converts an OpenCV {@link Mat} object to a Java {@link BufferedImage}.
     *
     * @param mat The matrix to be converted.
     * @return The converted {@link BufferedImage}.
     */
    private BufferedImage matToBufferdImageConverter(Mat mat){
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_BYTE_GRAY);
        mat.get(0,0,((DataBufferByte) image.getRaster().getDataBuffer()).getData());
        return image;
    }

    /**
     * Converts a Java {@link BufferedImage} to an OpenCV {@link Mat}.
     *
     * @param image The {@link BufferedImage} to be converted.
     * @return The converted {@link Mat}.
     */
    private Mat bufferedImageToMatConverter(BufferedImage image){
        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    /**
     * Converts a list of OpenCV {@link Rect} objects to Java {@link Rectangle} objects.
     *
     * @param rects The list of {@link Rect} objects.
     * @return A list of {@link Rectangle} objects.
     */
    private ArrayList<Rectangle> rectToRectangleConverter(ArrayList<Rect> rects){
        ArrayList<Rectangle> rectangles = new ArrayList<>();
        for (Rect rect : rects) {
            rectangles.add(new Rectangle(rect.x, rect.y, rect.width, rect.height));
        }
        return rectangles;
    }

    /**
     * Saves an uploaded image file to the specified upload directory.
     *
     * @param file The {@link MultipartFile} containing the image file to be saved.
     * @return The absolute path of the saved image file.
     */
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