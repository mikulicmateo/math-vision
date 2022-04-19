package com.pi.math_vision_android;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

public class ImageManipulation {

    private static int width;

    //cropanje slike newWidth i newHeight u postotcima
    public static Bitmap resizeImage(Bitmap bitmap, float newWidth, float newHeight) {
         int NewWidth= Math.round(bitmap.getWidth()*(newWidth/100));
         int NewHeight=Math.round(bitmap.getHeight()*(newHeight/100));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, NewWidth, NewHeight, true);
        return scaledBitmap;
    }
    public static Bitmap resizeImage(Bitmap bitmap) {
        //mijenjamo dole mijenjamo lijevo
        bitmap=rotateImage(bitmap,90);
        bitmap = Bitmap.createBitmap(bitmap,(int) Math.round(bitmap.getWidth()*0.05),(int)Math.round(bitmap.getHeight()*0.43),
                (int)Math.round(bitmap.getWidth()*0.9),(int)Math.round(bitmap.getHeight()*0.27));
        return bitmap;
    }

    //rotacija slike za stupnjeve za slucaj da treba rotirat i rezat sliku
    public static Bitmap rotateImage(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    //preokretanje slike oko orijentacije
    //za slucaj dodavanja flip botuna i rezanje slike sa vanjske strane je ovako brze
    //orijentacija 1 drugi kvadrant
    //orijentacija 2 treci  kvadrant
    //orijentacija 3 cetvrti kvadrant
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
        Bitmap flippedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return flippedBitmap;
    }
}

