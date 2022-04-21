package helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Size;

import androidx.core.app.ActivityCompat;

public class CameraHelper {

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    public static Size openCamera(CameraManager manager, Context context, Activity activity, CameraDevice.StateCallback stateCallback){
        try{
            String cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            Size imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            //Check realtime permission if run higher API 23
            if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },REQUEST_CAMERA_PERMISSION);
                return imageDimension; // TODO - treba li ovdje vratiti imageDimension?
            }
            manager.openCamera(cameraId,stateCallback,null);
            return imageDimension;

        } catch (CameraAccessException e) {
            e.printStackTrace();
            return null; // TODO - bolje samo baciti exception?
        }
    }
}
