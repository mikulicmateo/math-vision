package com.pi.math_vision_android.maker;

import android.graphics.Bitmap;
import android.util.Log;

import com.pi.math_vision_android.image_processing.ImageProcessingUtility;
import com.pi.math_vision_android.recognition_model.SymbolRecognitionModelInterface;

import java.util.List;

public class EquationMaker {

    public static String imageToEquation(List<Bitmap> wholeImageBitmapList){
        List<Bitmap> symbolImages = ImageProcessingUtility.preprocessImage(wholeImageBitmapList);
        StringBuilder equationBuilder = new StringBuilder();

        for(Bitmap symbolImage : symbolImages)
            equationBuilder.append(SymbolRecognitionModelInterface.getPrediction(symbolImage));

        String equation = clearEquation(equationBuilder);
        Log.e("Equation is:****************************", equation);
        return equation;
    }

    private static String clearEquation(StringBuilder stringBuilder){
        String equation = stringBuilder.toString().replace("--", "=");
        stringBuilder.delete(0, stringBuilder.length());
        int N = equation.length();
        int i = 0;

        while (i < N){
            char c = equation.charAt(i);
            stringBuilder.append(c);
            if(!Character.isDigit(c))
                while (i<N && equation.charAt(i)==c)
                    ++i;
            else
                ++i;
        }
        return stringBuilder.toString();
    }
}
