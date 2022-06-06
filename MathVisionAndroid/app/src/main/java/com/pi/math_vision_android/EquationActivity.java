package com.pi.math_vision_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jstarczewski.pc.mathview.src.MathView;

import com.pi.math_vision_android.containers.ImageContainer;
import com.pi.math_vision_android.helpers.CalculationHelper;

public class EquationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.equation_layout);
        MathView mvTest = (MathView) findViewById(R.id.MathView);
        Bundle bundle = getIntent().getExtras();
        String equation = bundle.getString("textEquation");
        equation = equation.replaceAll("=", "==");
        if (getResources().getString(R.string.mode).equals("night")) {
            mvTest.setBackgroundColor("#121212");
            mvTest.setTextColor("white");
        }
        mvTest.setText("Equation $$" + equation + "$$ solution:" + CalculationHelper.getAnswer(equation));
    }

    public void again(View v) {
        //changes the content view to MainClass activity
        Intent intent = new Intent(EquationActivity.this, MainActivity.class);
        ImageContainer.ImageByteArray.clear();
        EquationActivity.this.startActivity(intent);
        finish();
    }

}