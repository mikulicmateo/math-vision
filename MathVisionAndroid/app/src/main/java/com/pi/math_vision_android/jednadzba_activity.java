package com.pi.math_vision_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class jednadzba_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jednadzba_layout);
    }
    public void ponovo(View v)
    {
        Intent namjera = new Intent(jednadzba_activity.this,MainActivity.class);
        jednadzba_activity.this.startActivity(namjera);
    }
}