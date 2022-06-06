package com.pi.math_vision_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.pi.math_vision_android.containers.ImageContainer;
import com.pi.math_vision_android.maker.EquationMaker;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;
import java.util.List;


public class ConfirmActivity extends AppCompatActivity {

    private EditText equationText;
    private String equation;
    private List<Bitmap> bitmapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_confirm);
        equationText = findViewById(R.id.editTextEquation);
        ImageView showImage = findViewById(R.id.imageView);
        try {
            bitmapList = new ArrayList<>();
            for (byte[] byteImage : ImageContainer.ImageByteArray) {
                bitmapList.add(BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length));
            }
            showImage.setImageBitmap(bitmapList.get(4));
        } catch (NullPointerException e) {
            //onCreate(savedInstanceState);
        } catch (Exception e) {
            equationText.append(e.toString());
        }
    }

    public void clickRetry(View view) {
        // Returns to first activity
        Intent intent = new Intent(ConfirmActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ImageContainer.ImageByteArray.clear();
        startActivity(intent);
        finish();
    }

    private final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("OpenCV", "OpenCV loaded successfully!");
                    equation = EquationMaker.imageToEquation(bitmapList);
                    equationText.setText(equation);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };


    public void clickConfirm(View v) {
        Intent intent = new Intent(ConfirmActivity.this, EquationActivity.class);
        intent.putExtra("textEquation", equationText.getText().toString());
        ConfirmActivity.this.startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // OpenCV manager initialization
        OpenCVLoader.initDebug();
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }
}