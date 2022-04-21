package com.pi.math_vision_android.image_processing;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ImageProcessingUtility {

    private Mat readImage(Bitmap bitmap){
        Imgcodecs imageCodecs = new Imgcodecs();
        imageCodecs.imread(); // =
        Mat mat = new Mat()
        Utils.bitmapToMat(bitmap, mat);

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY); //grayscale
        Imgproc.blur(mat,mat, new Size(5,5));
        Imgproc.threshold(mat, mat, 125, 250, Imgproc.THRESH_BINARY_INV); // thresholding

        Mat rectKernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(2,2));
        Imgproc.dilate(mat, mat, rectKernel, new Point(-1,-1), 3); //dilation

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE); //find contours



    }


}
