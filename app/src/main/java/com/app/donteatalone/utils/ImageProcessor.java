package com.app.donteatalone.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.donteatalone.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ChomChom on 03-Nov-17
 */

public class ImageProcessor {

    public static final int PERMISSION_CAMERA_REQUEST_CODE = 100;
    public static final int PERMISSION_READ_REQUEST_CODE = 101;
    public static final int RESULT_TAKE_PHOTO = 0;
    public static final int RESULT_CHOOSE_IMAGE = 1;
    public static final int RESULT_CROP = 2;
    private static final String CROP_INTENT = "com.android.camera.action.CROP";

    public static String convertBitmapToString(Bitmap bitmap) {
        String tempConvert = "";
        if (bitmap != null) {
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, arrayOutputStream);
            byte[] b = arrayOutputStream.toByteArray();
            tempConvert = Base64.encodeToString(b, Base64.DEFAULT);
        }
        return tempConvert;
    }

    public static byte[] convertBitmapToByte(Bitmap bitmap) {

        if (bitmap != null) {
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayOutputStream);
            return arrayOutputStream.toByteArray();
        }
        return null;
    }

    public static Bitmap decodeBitmap(String str) {
        Bitmap bitmap = null;
        try {
            byte[] encodeByte = Base64.decode(str, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
        }
        return bitmap;
    }

    public static Bitmap definiteRotate(String path, Bitmap bitmap) {
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

    private static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static Bitmap resizeBitmap(Bitmap bitmap, Context context) {
        if (bitmap.getWidth() < bitmap.getHeight()) {
            return Bitmap.createScaledBitmap(bitmap, (int) context.getResources().getDimension(R.dimen.image_size),
                    (int) (context.getResources().getDimension(R.dimen.image_size) * bitmap.getHeight()) / bitmap.getWidth(), true);
        } else {
            return Bitmap.createScaledBitmap(bitmap, (int) (context.getResources().getDimension(R.dimen.image_size) * bitmap.getHeight()) / bitmap.getWidth(), (int) context.getResources().getDimension(R.dimen.image_size), true);
        }
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, Activity activity) {
        DisplayMetrics screen = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(screen);
        int width = screen.widthPixels;

        bitmap = Bitmap.createScaledBitmap(bitmap, width, bitmap.getHeight() * width / bitmap.getWidth(), true);
        return bitmap;

    }


    public static void performCrop(Uri picUri, Fragment fragment, Activity activity) {
        Intent cropIntent = new Intent(CROP_INTENT);
        cropIntent.setDataAndType(picUri, "image/*");
        List<ResolveInfo> list;
        if (fragment != null) {
            list = fragment.getActivity().getPackageManager().queryIntentActivities(cropIntent, 0);
        } else {
            list = activity.getPackageManager().queryIntentActivities(cropIntent, 0);
        }
        int size = list.size();
        if (size == 0) {
            if (fragment != null) {
                Toast.makeText(fragment.getActivity(), fragment.getActivity().getString(R.string.dont_find_image), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, activity.getString(R.string.dont_find_image), Toast.LENGTH_SHORT).show();
            }
        } else {
            try {

                cropIntent.putExtra("crop", "true");
                cropIntent.putExtra("aspectX", 10);
                cropIntent.putExtra("aspectY", 9);
                cropIntent.putExtra("outputX", 500);
                cropIntent.putExtra("outputY", 500);
                cropIntent.putExtra("return-data", true);
                if (fragment != null) {
                    fragment.startActivityForResult(cropIntent, RESULT_CROP);
                } else {
                    activity.startActivityForResult(cropIntent, RESULT_CROP);
                }
            } catch (ActivityNotFoundException e) {
                if (fragment != null) {
                    Toast.makeText(fragment.getActivity(), fragment.getActivity().getString(R.string.not_support_crop), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, activity.getString(R.string.not_support_crop), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public static void requestPermissionsResult(int requestCode, int grantResults, Fragment fragment, Activity activity) {
        if (requestCode == PERMISSION_READ_REQUEST_CODE) {

            if (grantResults == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                if (activity!=null){
                    activity.startActivityForResult(intent, RESULT_CHOOSE_IMAGE);
                }else {
                    fragment.startActivityForResult(intent, RESULT_CHOOSE_IMAGE);
                }

            } else {
                if (activity!=null){
                    Toast.makeText(activity, activity.getResources().getString(R.string.deny_read_storage), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(fragment.getActivity(), fragment.getActivity().getResources().getString(R.string.deny_read_storage), Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (activity!=null){
                    activity.startActivityForResult(intent, RESULT_TAKE_PHOTO);
                }else {
                    fragment.startActivityForResult(intent, RESULT_TAKE_PHOTO);
                }

            } else {
                if (activity!=null){
                    Toast.makeText(activity, activity.getResources().getString(R.string.deny_camera), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(fragment.getActivity(), fragment.getActivity().getResources().getString(R.string.deny_camera), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public static void activityImageResult(int resultCode, int requestCode, Intent data, Fragment fragment, Activity activity, ImageView imgAvatar) {
        Bitmap bitmap = null;

        if (data != null) {
            if (resultCode == RESULT_OK) {
                if (requestCode == RESULT_TAKE_PHOTO) {
                    if (data.getData() != null) {

                        bitmap = (Bitmap) data.getExtras().get("data");

                        bitmap = ImageProcessor.definiteRotate(data.getData().getPath(), bitmap);

                        if (activity!=null){
                            ImageProcessor.performCrop(data.getData(), null, activity);
                        }else {
                            ImageProcessor.performCrop(data.getData(), fragment, null);
                        }

                    }
                } else if (requestCode == RESULT_CHOOSE_IMAGE) {
                    if (data.getData() != null) {
                        try {
                            Cursor cursor;
                            if(activity!=null){
                                bitmap = MediaStore.Images.Media.getBitmap(activity.getApplicationContext().getContentResolver(), data.getData());

                                cursor=activity.getContentResolver().query(data.getData(), null, null, null, null);
                            }else {
                                bitmap = MediaStore.Images.Media.getBitmap(fragment.getActivity().getApplicationContext().getContentResolver(), data.getData());

                                cursor=fragment.getActivity().getContentResolver().query(data.getData(), null, null, null, null);
                            }

                            if (cursor == null) {
                                bitmap = ImageProcessor.definiteRotate(data.getData().getPath(), bitmap);
                            } else {
                                cursor.moveToFirst();
                                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                                bitmap = ImageProcessor.definiteRotate(cursor.getString(idx), bitmap);
                                cursor.close();
                            }

                            if (activity!=null){
                                ImageProcessor.performCrop(data.getData(), null, activity);
                            }else {
                                ImageProcessor.performCrop(data.getData(), fragment, null);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (requestCode == RESULT_CROP) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        bitmap = extras.getParcelable("data");
                        imgAvatar.setImageBitmap(bitmap);
                    }
                }

                if (bitmap != null) {
                    if (activity!=null){
                        bitmap = ImageProcessor.resizeBitmap(bitmap, activity);
                    }else {
                        bitmap = ImageProcessor.resizeBitmap(bitmap, fragment.getActivity());
                    }

                    imgAvatar.setImageBitmap(bitmap);
                }
            }
        }
    }
}
