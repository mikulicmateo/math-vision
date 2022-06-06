package com.pi.math_vision_android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pi.math_vision_android.containers.ImageContainer;
import com.pi.math_vision_android.helpers.CameraHelper;
import com.pi.math_vision_android.maker.EquationMaker;

import java.util.Base64;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static Context appContext;
    private Button btnCapture;
    private TextureView cameraPreview;
    private ImageButton button_flash;

    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureRequestBuilder;

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private boolean isFlashOn = false;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        appContext = getApplicationContext();

        cameraPreview = findViewById(R.id.cameraPreview);
        assert cameraPreview != null;
        cameraPreview.setSurfaceTextureListener(textureListener);

        button_flash = (ImageButton) findViewById(R.id.button_flash);
        btnCapture = findViewById(R.id.buttonTakePicture);

        // On click Take picture and start new activity
        btnCapture.setOnClickListener(view -> {

            if (cameraDevice == null)
                return;

            for(int i = 0; i < 5; i++){
                ImageContainer.ImageByteArray.add(CameraHelper.takePicture(cameraPreview));
            }

            if (isFlashOn) {
                actionFlash(view);
            }

            Intent intent = new Intent(MainActivity.this, ConfirmActivity.class);

            startActivity(intent);
            finish();
        });
    }


    public static Context getAppContext() {
        return appContext;
    }

    private void createCameraPreview() {
        //Method for showing camera view
        try {
            SurfaceTexture texture = cameraPreview.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(CameraHelper.getImageDimension().getWidth(), CameraHelper.getImageDimension().getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if (cameraDevice == null)
                        return;
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(MainActivity.this, "Changed", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        //Method for rendering camera lens output
        if (cameraDevice == null)
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, new Handler());
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
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Method which checks if user gave permission for using camera
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You can't use camera without permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void actionFlash(View view) {
        try {
            if (isFlashOn) {
                button_flash.setImageResource(R.drawable.ic_flash_on);
                cameraCaptureSessions.stopRepeating();
                captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                updatePreview();
                isFlashOn = false;
            } else {
                button_flash.setImageResource(R.drawable.ic_flash_off);
                cameraCaptureSessions.stopRepeating();
                captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                updatePreview();
                isFlashOn = true;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
