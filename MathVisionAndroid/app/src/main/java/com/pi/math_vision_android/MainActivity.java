package com.pi.math_vision_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.hardware.camera2.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("hello", "hello world");
    }

    // This method calls when button "Slikaj" is clicked
    public void clickSlikaj(View view) {
        LinearLayout layoutCamera = findViewById(R.id.cameraLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.weight = 0.5f;
        layoutCamera.setLayoutParams(params);

    }
}