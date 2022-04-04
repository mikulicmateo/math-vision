package com.pi.math_vision_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
//import android.graphics.Camera;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    // Ovo je iz hardeare.camera
    Camera camera;
    FrameLayout cameraLayout;
    ShowCamera showCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraLayout =(FrameLayout) findViewById(R.id.cameraLayout);

        //Open back camera
        camera = Camera.open();

        showCamera = new ShowCamera(this, camera);
        cameraLayout.addView(showCamera);

    }




    // This method calls when button "Take picture" is clicked
    public void clickTakePicture(View view) {

    }

}