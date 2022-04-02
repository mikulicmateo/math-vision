package com.pi.math_vision_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class jednazdba_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jednadzba_layout);
    }
    public void ponovo(View v)
    {
        Intent namjera = new Intent(jednazdba_activity.this,MainActivity.class);
        jednazdba_activity.this.startActivity(namjera);
    }

}