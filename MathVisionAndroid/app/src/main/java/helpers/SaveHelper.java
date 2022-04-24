package helpers;

import android.annotation.SuppressLint;
import android.media.Image;
import android.media.ImageReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveHelper {

    public static void readAndSavePicture(ImageReader reader, File file){
        Image image = null;
        try{
            image = reader.acquireLatestImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.capacity()];
            buffer.get(bytes);
            savePicture(bytes, file);

        } catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Reading and saving image not possible");
        } finally {
            {
                if(image != null)
                    image.close();
            }
        }
    }

    public static void savePicture(byte[] bytes, File file) throws IOException{
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(bytes);
        }
    }

    public static void createOrExistsFolder(String folder){
        File directory = new File(folder);
        if(!directory.exists()){
            directory.mkdir();
        }
    }

    public static String createDateString(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }
}
