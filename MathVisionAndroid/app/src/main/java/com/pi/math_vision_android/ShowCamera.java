package com.pi.math_vision_android;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {
    Camera camera;
    SurfaceHolder holder;

    public ShowCamera(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        Camera.Parameters params = camera.getParameters();

        //Get camera picture size
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size mSize = null;

        for(Camera.Size size : sizes){
            mSize = size;
        }

        //Change the orientation of the camera
        if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
            params.set("orientation", "portrait");
            camera.setDisplayOrientation(90);
            params.setRotation(90);
        }

        params.setPictureSize(mSize.width, mSize.height);
        camera.setParameters(params);
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
