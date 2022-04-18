package com.pi.math_vision_android.recognition_model;

import android.graphics.Bitmap;

import com.pi.math_vision_android.MainActivity;
import com.pi.math_vision_android.ml.NumberRecognizer;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class SymbolRecognitionModelInterface {

    public char getPrediction(Bitmap img){
        try {
            NumberRecognizer model = NumberRecognizer.newInstance(MainActivity.getAppContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature = TensorBuffer.createFixedSize(new int[]{1, 100, 100, 1}, DataType.FLOAT32);

            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
            tensorImage.load(img);
            ByteBuffer byteBuffer = tensorImage.getBuffer();

            inputFeature.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            NumberRecognizer.Outputs outputs = model.process(inputFeature);
            TensorBuffer outputFeature = outputs.getOutputFeature0AsTensorBuffer();

            char symbol = getPredictedSymbol(outputFeature.getFloatArray());
            // Releases model resources if no longer used.
            model.close();

            return symbol;
        } catch (IOException e) {
            throw new RuntimeException("Bad input has been given to the model!");
        }
    }

    private char getPredictedSymbol(float[] outputFeature){

        float max = 0;
        int classIndex = 0;
        for(int i = 0; i < outputFeature.length; i++){
            if(outputFeature[i] > max){
                max = outputFeature[i];
                classIndex = i;
            }
        }

        return mapCharacter(classIndex);
    }

    private char mapCharacter(int classIndex) {
        return SymbolRecognitionModelClassConstants.CLASSES_CHARS[classIndex];
    }
}
