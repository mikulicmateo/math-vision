package com.pi.math_vision_android.recognition_model;

import android.graphics.Bitmap;

import com.pi.math_vision_android.MainActivity;
import com.pi.math_vision_android.constants.SymbolRecognitionModelClassConstants;
import com.pi.math_vision_android.image_processing.ImageProcessingUtility;
import com.pi.math_vision_android.ml.NumberRecognizer;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class SymbolRecognitionModelInterface {

    public static char getPrediction(Bitmap img) {
        try {
            NumberRecognizer model = NumberRecognizer.newInstance(MainActivity.getAppContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature = TensorBuffer.createFixedSize(new int[]{1, 100, 100, 1}, DataType.FLOAT32);

            ByteBuffer byteBuffer = ImageProcessingUtility.getByteBufferFromBitmap(img);

            inputFeature.loadBuffer(byteBuffer, new int[]{1, 100, 100, 1});

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

    private static char getPredictedSymbol(float[] outputFeature) {

        float max = 0;
        int classIndex = 0;
        for (int i = 0; i < outputFeature.length; i++) {
            if (outputFeature[i] > max) {
                max = outputFeature[i];
                classIndex = i;
            }
        }

        return mapCharacter(classIndex);
    }

    private static char mapCharacter(int classIndex) {
        return SymbolRecognitionModelClassConstants.CLASSES_CHARS[classIndex];
    }
}
