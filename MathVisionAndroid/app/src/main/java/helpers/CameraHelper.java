package helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
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
import android.media.ImageReader;
import android.os.Handler;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import callbacks.CaptureStateCallback;
import constants.OrientationConstants;
import containers.SizeContainer;

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

    public static ImageReader capturePicture(CameraManager cameraManager, CameraDevice cameraDevice, TextureView cameraPreview,
                                             int rotation, Context context, CameraCaptureSession.CaptureCallback captureListener) throws CameraAccessException {

        if(cameraDevice == null)
            throw new CameraAccessException(CameraAccessException.CAMERA_ERROR);

        CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraDevice.getId());
        Size[] jpegSizes = checkCharacteristicsAndGetSizes(characteristics);

        SizeContainer sizeContainer = getJpegWidthAndHeight(jpegSizes);

        ImageReader reader = ImageReader
                .newInstance(sizeContainer.getWidth(), sizeContainer.getHeight(), ImageFormat.JPEG, 1);

        List<Surface> outputSurface = new ArrayList<>(2);
        outputSurface.add(reader.getSurface());
        outputSurface.add(new Surface(cameraPreview.getSurfaceTexture()));

        CaptureRequest.Builder captureBuilder  = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        captureBuilder.addTarget(reader.getSurface());
        captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, OrientationConstants.ORIENTATIONS.get(rotation));

        cameraDevice.createCaptureSession(outputSurface, new CaptureStateCallback(captureListener, captureBuilder), new Handler());

        return reader;
    }

    private static SizeContainer getJpegWidthAndHeight(Size[] jpegSizes) {
        SizeContainer container = new SizeContainer();
        if(jpegSizes != null && jpegSizes.length > 0){
            container.setWidth(jpegSizes[0].getWidth());
            container.setHeight(jpegSizes[0].getHeight());
        }
        return container;
    }

    private static Size[] checkCharacteristicsAndGetSizes(CameraCharacteristics characteristics){
        if(characteristics != null){
            return characteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    .getOutputSizes(ImageFormat.JPEG);
        }
        return null;
    }
}
