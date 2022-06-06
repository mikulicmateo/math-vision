package com.pi.math_vision_android.image_processing;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class ImageProcessingUtility {


    public static List<Bitmap> preprocessImage(List<Bitmap> wholeImageBitmapList) throws RuntimeException {
        List<Mat> images = new ArrayList<>();

        wholeImageBitmapList.forEach(bitmap -> images.add(readImageToGrayScale(bitmap)));

        Mat averagedImage = new Mat();
        Photo.fastNlMeansDenoisingMulti(images, averagedImage, 2, 5);

        Mat augmentedImage = augmentImage(averagedImage);
        return getIndividualSymbols(augmentedImage);
    }

    private static Mat readImageToGrayScale(Bitmap bitmap) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);

        return mat;
    }

    private static List<Bitmap> getIndividualSymbols(Mat image) throws RuntimeException {
        List<Bitmap> symbols = new ArrayList<>();

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(image, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE); //find contours

        if (contours.size() >= 20)
            throw new RuntimeException("Error: Too many symbols recognised");

        for (Rect rect : getBoundingRectangles(contours)) {
            Mat imageToResize = new Mat(image, rect);
            Mat blankImage = Mat.zeros(new Size(100, 100), CvType.CV_8U);

            while (imageToResize.height() > 100 || imageToResize.width() > 100) {
                Imgproc.resize(imageToResize, imageToResize, new Size(0, 0), 0.5, 0.5, Imgproc.INTER_AREA);
            }

            int x = 50 - (imageToResize.width() / 2);
            int y = 50 - (imageToResize.height() / 2);

            imageToResize.copyTo(blankImage.colRange(x, x + imageToResize.width()).rowRange(y, y + imageToResize.height()));
            Core.bitwise_not(blankImage, blankImage);

            Bitmap bmp;
            Mat tmp = new Mat(blankImage.rows(), blankImage.cols(), CvType.CV_8UC1, new Scalar(4));
            Imgproc.cvtColor(blankImage, tmp, Imgproc.COLOR_GRAY2RGBA, 4);
            bmp = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tmp, bmp);

            symbols.add(bmp);
        }

        return symbols;
    }

    private static Mat augmentImage(Mat mat) {
        Imgproc.blur(mat, mat, new Size(5, 5));
        Imgproc.threshold(mat, mat, 125, 250, Imgproc.THRESH_BINARY_INV); // thresholding
        Mat rectKernelErosion = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(2, 2));
        Mat rectKernelDilation = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(3, 3));
        Imgproc.erode(mat, mat, rectKernelErosion, new Point(-1, -1), 2); //eroding
        Imgproc.dilate(mat, mat, rectKernelDilation, new Point(-1, -1), 5); //dilation

        return mat;
    }

    private static List<Rect> getBoundingRectangles(List<MatOfPoint> contours) {
        contours.sort(new MatOfPointComparator());
        List<Rect> boundingRects = new ArrayList<>();

        for (MatOfPoint contour : contours) {
            boundingRects.add(Imgproc.boundingRect(contour));
        }
        return boundingRects;
    }

    public static ByteBuffer getByteBufferFromBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        ByteBuffer mImgData = ByteBuffer.allocateDirect(4 * width * height);

        mImgData.order(ByteOrder.nativeOrder());
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int pixel : pixels) {
            mImgData.putFloat((float) Color.red(pixel));
        }
        return mImgData;
    }
}
