package com.pi.math_vision_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import com.jstarczewski.pc.mathview.src.MathView;

public class EquationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.equation_layout);
        Calculation calc = new Calculation();
        MathView mvTest = (MathView) findViewById(R.id.MathView);
        Bundle bundle = getIntent().getExtras();
        String equation = bundle.getString("equation");

            if(getResources().getString(R.string.mode).equals("night")){
            mvTest.setBackgroundColor("lightDark");
            mvTest.setTextColor("white");}
        mvTest.setText("Equation $$"+equation+"$$ solution:"+calc.getAnswer(equation));


    }
    public void again(View v)
    {
        //changes the content view to MainClass activity
        Intent intent = new Intent(EquationActivity.this,MainActivity.class);
        EquationActivity.this.startActivity(intent);
    }

}