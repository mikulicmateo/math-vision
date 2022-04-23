package listeners;

import android.media.ImageReader;

import java.io.File;

import helpers.SaveHelper;

public class ImageListener implements ImageReader.OnImageAvailableListener {

    private File file;

    public ImageListener(File file){
        this.file = file;
    }

    @Override
    public void onImageAvailable(ImageReader imageReader) {
        SaveHelper.readAndSavePicture(imageReader, file);
    }
}
