package com.pi.math_vision_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import com.jstarczewski.pc.mathview.src.MathView;

public class JednadzbaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jednadzba_layout);
        Racunanje calc = new Racunanje();
        MathView mvTest = (MathView) findViewById(R.id.MathView);
        Log.e("tekstic", getResources().getString(R.string.mode));
            if(getResources().getString(R.string.mode).equals("night")){
            mvTest.setBackgroundColor("black");
            mvTest.setTextColor("white");}
        mvTest.setText("riješenje jednadžbe:"+"$$jednadzba ovdje$$ je "+calc.getAnswer("1920-1080"));//testni kod zamjeni se varijablama kada ga budemo imali


    }
    public void ponovo(View v)
    {
        Intent namjera = new Intent(JednadzbaActivity.this,MainActivity.class);
        JednadzbaActivity.this.startActivity(namjera);
    }

}