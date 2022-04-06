package com.pi.math_vision_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
//import android.graphics.Camera;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // Ovo je iz hardeare.camera
    Camera camera;
    FrameLayout cameraLayout;
    ShowCamera showCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraLayout =(FrameLayout) findViewById(R.id.cameraLayout);

        //Open back camera
        camera = Camera.open();

        showCamera = new ShowCamera(this, camera);
        cameraLayout.addView(showCamera);
        galleryAddPic();

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "imageDirectory");
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            File picture_file = getOutputMediaFile();

            if(picture_file  == null){
                return;
            }else {
                try {
                    FileOutputStream fos = new FileOutputStream(picture_file);
                    fos.write(bytes);
                    fos.close();

                    camera.startPreview();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)){
            return null;
        }else{
            File folder_image = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "imageDirectory");
            if(!folder_image.exists()){
                folder_image.mkdirs();
            }

            File outputFile = new File(folder_image,"temp.jpg");
            return outputFile;
        }
    }



    // This method calls when button "Take picture" is clicked
    public void clickTakePicture(View view) {
        if(camera != null){
            camera.takePicture(null,null,mPictureCallback);
        }
    }

}