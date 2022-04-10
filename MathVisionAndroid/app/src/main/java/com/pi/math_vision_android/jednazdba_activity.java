package com.pi.math_vision_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import com.jstarczewski.pc.mathview.src.MathView;

public class jednazdba_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("rest","test");
        setContentView(R.layout.jednadzba_layout);
        MathView mvTest = (MathView) findViewById(R.id.MathView);
        mvTest.setBackgroundColor("$\\F&#x2196{&#x2192}=ma_g$");


    }
    public void ponovo(View v)
    {
        Intent namjera = new Intent(jednazdba_activity.this,MainActivity.class);
        jednazdba_activity.this.startActivity(namjera);
    }

}