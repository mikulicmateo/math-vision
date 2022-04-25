package com.pi.math_vision_android.callbacks;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;

import androidx.annotation.NonNull;

public class CaptureStateCallback extends CameraCaptureSession.StateCallback {

    private CameraCaptureSession.CaptureCallback captureListener;
    private CaptureRequest.Builder captureBuilder;

    public CaptureStateCallback(CameraCaptureSession.CaptureCallback captureCallback, CaptureRequest.Builder captureBuilder){
        this.captureListener = captureCallback;
        this.captureBuilder = captureBuilder;
    }

    @Override
    public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
        try{
            cameraCaptureSession.capture(captureBuilder.build(),captureListener,new Handler());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

    }
}
