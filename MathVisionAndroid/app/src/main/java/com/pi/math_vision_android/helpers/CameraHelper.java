    package com.pi.math_vision_android.helpers;

    import android.Manifest;
    import android.app.Activity;
    import android.content.Context;
    import android.content.pm.PackageManager;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.ImageFormat;
    import android.graphics.SurfaceTexture;
    import android.hardware.camera2.CameraAccessException;
    import android.hardware.camera2.CameraCaptureSession;
    import android.hardware.camera2.CameraCharacteristics;
    import android.hardware.camera2.CameraDevice;
    import android.hardware.camera2.CameraManager;
    import android.hardware.camera2.CameraMetadata;
    import android.hardware.camera2.CaptureRequest;
    import android.hardware.camera2.params.StreamConfigurationMap;
    import android.media.Image;
    import android.media.ImageReader;
    import android.os.Handler;
    import android.os.Looper;
    import android.util.Log;
    import android.util.Size;
    import android.view.PixelCopy;
    import android.view.Surface;
    import android.view.SurfaceView;
    import android.view.TextureView;

    import androidx.core.app.ActivityCompat;

    import java.util.ArrayList;
    import java.util.List;

    import com.pi.math_vision_android.callbacks.CaptureStateCallback;
    import com.pi.math_vision_android.constants.OrientationConstants;
    import com.pi.math_vision_android.containers.SizeContainer;

    public class CameraHelper {

        private static final int REQUEST_CAMERA_PERMISSION = 200;
        private static Size imageDimension;

        public static void openCamera(CameraManager manager, Context context, Activity activity, CameraDevice.StateCallback stateCallback){
            try{
                String cameraId = manager.getCameraIdList()[0];
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                assert map != null;
                imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

                //Check realtime permission if run higher API 23
                if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(activity, new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },REQUEST_CAMERA_PERMISSION);
                }
                manager.openCamera(cameraId,stateCallback,null);

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        public static Size getImageDimension() {
            return imageDimension;
        }

    }
