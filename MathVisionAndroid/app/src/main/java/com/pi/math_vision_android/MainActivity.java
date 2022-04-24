package com.pi.math_vision_android;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;

import java.io.File;

import android.Manifest;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.ImageReader;
import android.os.Handler;
import androidx.core.app.ActivityCompat;


import android.view.Surface;
import android.view.TextureView;
import android.widget.Button;
import android.widget.Toast;


import java.util.Collections;
import java.util.Objects;

import android.provider.Settings;
import android.net.Uri;

import helpers.CameraHelper;
import helpers.SaveHelper;
import listeners.ImageListener;

public class MainActivity extends AppCompatActivity {

    private Button btnCapture;
    private TextureView cameraPreview;

    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureRequestBuilder;

    //Save to FILE
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        //Asking users for permission to use storage
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                },
                1
        );

        cameraPreview = findViewById(R.id.cameraPreview);
        assert cameraPreview != null;
        cameraPreview.setSurfaceTextureListener(textureListener);

        // On click Take picture and start new activity
        btnCapture = findViewById(R.id.buttonTakePicture);
        btnCapture.setOnClickListener(view -> {
            takePicture();

            //Timer for delaying opening new activity
            new CountDownTimer(500, 50) {
                public void onFinish() {
                    // When timer is finished
                    // This is for opening ConfirmActivity
                    Intent intent = new Intent(MainActivity.this, ConfirmActivity.class);
                    intent.putExtra("ImagePath", file.toString());
                    startActivity(intent);
                }

                public void onTick(long millisUntilFinished) {
                    // millisUntilFinished The amount of time until finished.
                }
            }.start();
        });
    }


    private void takePicture() {
        // Method called on button click
        // Create, save and show picture

        if(cameraDevice == null)
            return;
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try{

            //Making Math-vision folder in pictures
            String folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + "Math-Vision";
            SaveHelper.createOrExistsFolder(folder);

            //Path where picture is stored
            String date = SaveHelper.createDateString();
            file = new File(folder + File.separator + "Picture_" + date + ".jpg");

            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    // Showing location of picture
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(MainActivity.this, "Saved: "+file, Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                }
            };

            ImageReader reader = CameraHelper.capturePicture(manager, cameraDevice, cameraPreview, getWindowManager().getDefaultDisplay().getRotation(),this, captureListener);

            //create image listener
            ImageListener readerListener = new ImageListener(file);

            reader.setOnImageAvailableListener(readerListener, new Handler());

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void createCameraPreview() {
        //Function for showing camera view
        try{
            SurfaceTexture texture = cameraPreview.getSurfaceTexture();
            assert  texture != null;
            texture.setDefaultBufferSize(CameraHelper.getImageDimension().getWidth(),CameraHelper.getImageDimension().getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if(cameraDevice == null)
                        return;
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(MainActivity.this, "Changed", Toast.LENGTH_SHORT).show();
                }
            },null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        // Function for getting back to activity and checking camera
        if(cameraDevice == null)
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE,CaptureRequest.CONTROL_MODE_AUTO);
        try{
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(),null, new Handler());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void openCameraAndInitializeImageDimension() {
        CameraHelper.openCamera((CameraManager) getSystemService(Context.CAMERA_SERVICE), this, this, this.stateCallback);
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        // Checks textureView and opens camera
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
         openCameraAndInitializeImageDimension();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {}

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {}
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Function which checks if user gave permission for using camera
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You can't use camera without permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
