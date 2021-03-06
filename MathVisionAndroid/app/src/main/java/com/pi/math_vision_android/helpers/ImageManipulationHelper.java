package com.pi.math_vision_android.helpers;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

public class ImageManipulationHelper {

    //Cropping image to newWidth and newHeight to given percentage
    public static Bitmap resizeImage(Bitmap bitmap, float newWidth, float newHeight) {
        int NewWidth = Math.round(bitmap.getWidth() * (newWidth / 100));
        int NewHeight = Math.round(bitmap.getHeight() * (newHeight / 100));
        return Bitmap.createScaledBitmap(bitmap, NewWidth, NewHeight, true);
    }

    public static Bitmap resizeImage(Bitmap bitmap) {
        //Cropping image to view bounding box

        bitmap = Bitmap.createBitmap(bitmap, (int) Math.round(bitmap.getWidth() * 0.05), (int) Math.round(bitmap.getHeight() * 0.43),
                (int) Math.round(bitmap.getWidth() * 0.9), (int) Math.round(bitmap.getHeight() * 0.27));
        return bitmap;
    }

    //Rotating image to given degree
    public static Bitmap rotateImage(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //Rotating image around the plane
    //Orientation 1 second quadrant
    //Orientation 2 third  quadrant
    //Orientation 3 fourth quadrant
    public static Bitmap flipImage(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        if (orientation == 0) {
            matrix.postScale(-1, 1);
        } else if (orientation == 1) {
            matrix.postScale(1, -1);
        } else if (orientation == 2) {
            matrix.postScale(-1, -1);
        } else if (orientation == 3) {
            matrix.postScale(1, 1);
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();
    }
}

