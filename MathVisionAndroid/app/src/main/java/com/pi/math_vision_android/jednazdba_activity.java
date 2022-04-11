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
        Racunanje calc = new Racunanje();
        MathView mvTest = (MathView) findViewById(R.id.MathView);
        ;
        mvTest.setText("riješenje jednadžbe:"+"$${x^2=4,x+y^2=6}$$ je "+calc.getAnswer("Solve({x^2==4,x+y^2==6}, {x,y})"));//testni kod zamjeni se varijablama kada ga budemo imali


    }
    public void ponovo(View v)
    {
        Intent namjera = new Intent(jednazdba_activity.this,MainActivity.class);
        jednazdba_activity.this.startActivity(namjera);
    }

}