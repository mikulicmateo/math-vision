package com.pi.math_vision_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pi.math_vision_android.Maker.EquationMaker;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import com.pi.math_vision_android.helpers.ImageManipulationHelper;

public class ConfirmActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private TextView textViewFormula;
    private String equation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_confirm);
        textViewFormula = findViewById(R.id.textViewFormula);
        ImageView showImage = findViewById(R.id.imageView);

    try {
        // Getting path of image
        Bundle extras = getIntent().getExtras();
        String imagePath = extras.getString("ImagePath");

        // Creating image view from path
        Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
        this.bitmap = ImageManipulationHelper.resizeImage(myBitmap);
        showImage.setImageBitmap(bitmap);
    }
    catch(NullPointerException e){
                onCreate(savedInstanceState);
    }
    catch (Exception e) {
        textViewFormula.append(e.toString());
    }

    }

    public void clickRetry(View view)
    {
        // Returns to first activity
        Intent intent = new Intent(ConfirmActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("OpenCV", "OpenCV loaded successfully!");
                    equation=EquationMaker.imageToEquation(bitmap);
                    textViewFormula.setText(equation);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
    public void clickConfirm(View v)
    {
        Intent intent = new Intent(ConfirmActivity.this,EquationActivity.class);
        intent.putExtra("equation", equation);
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