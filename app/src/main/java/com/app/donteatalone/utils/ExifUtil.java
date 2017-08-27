package com.app.donteatalone.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

/**
 * Created by ChomChom on 17-Aug-17.
 */

public class ExifUtil {
    public static Bitmap definiteRotate(String path, Bitmap bitmap){
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = ExifInterface.ORIENTATION_NORMAL;

        if (exif != null)
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotateBitmap(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap = rotateBitmap(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                bitmap = rotateBitmap(bitmap, 270);
                break;
        }
        return bitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    public static Bitmap scaleBitmap(Bitmap bitmap, Activity activity){
        int width=activity.getWindowManager().getDefaultDisplay().getWidth();
        int height=activity.getWindowManager().getDefaultDisplay().getHeight();
        if(bitmap.getWidth()<width/2){
            return bitmap;
        }
        else {
            bitmap=Bitmap.createScaledBitmap(bitmap,width/2,bitmap.getHeight()*width/(2*bitmap.getWidth()),true);
            return bitmap;
        }
    }
}
