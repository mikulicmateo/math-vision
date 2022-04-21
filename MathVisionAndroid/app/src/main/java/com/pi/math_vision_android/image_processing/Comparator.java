package com.pi.math_vision_android.image_processing;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class Comparator implements java.util.Comparator<MatOfPoint> {


    @Override
    public int compare(MatOfPoint o1, MatOfPoint o2) {
        Rect rect1 = Imgproc.boundingRect(o1);
        Rect rect2 = Imgproc.boundingRect(o2);
        int result = 0;
        double total = rect1.tl().y/rect2.tl().y;
        if (total>=0.9 && total<=1.4 ){
            result = Double.compare(rect1.tl().x, rect2.tl().x);
        }
        return result;
    }
}
