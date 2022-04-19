package com.pi.math_vision_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_confirm);
        TextView textView = findViewById(R.id.textViewFormula);
        ImageView showImage = findViewById(R.id.imageView);

    try {
        Bundle extras = getIntent().getExtras();
        String imagePath = extras.getString("ImagePath");

        Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
        ImageManipulation manip = new ImageManipulation();
        Bitmap test = manip.resizeImage(myBitmap);
        showImage.setImageBitmap(test);
    }
    catch(NullPointerException e){
                onCreate(savedInstanceState);
    }
    catch (Exception e) {
        textView.append(e.toString());
    }

    }

    public void clickRetry(View view)
    {
        Intent intent = new Intent(ConfirmActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}