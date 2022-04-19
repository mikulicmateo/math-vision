package com.pi.math_vision_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent namjera = new Intent(MainActivity
                .this, JednadzbaActivity.class);//stavljeno za test maknuti mijenja layoput na jendadzbu
        MainActivity.this.startActivity(namjera);//
        Log.e("hello", "hello world");
    }
}