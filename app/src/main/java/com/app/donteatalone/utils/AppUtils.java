package com.app.donteatalone.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.MultiAutoCompleteTextView;

import java.io.ByteArrayOutputStream;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * -> Created by LeHoangHan on 4/4/2017.
 */

public class AppUtils {

    public static Spanned formHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    /*Get Status Bar Height*/
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelOffset(resourceId);
        }
        return result;
    }

    /*Making notification bar transparent*/
    public static void changeStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    //Hide keyboard when touch outside
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(activity.getCurrentFocus()!=null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static String convertBitmaptoString(Bitmap bitmap) {
        String tempConvert = "";
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, arrayOutputStream);
        byte[] b = arrayOutputStream.toByteArray();
        tempConvert = Base64.encodeToString(b, Base64.DEFAULT);
        return tempConvert;
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

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    public static String isNull(String str){
        if(str==null){
            return "";
        }
        else {
            return str;
        }
    }

    public static String convertStringToNFD(String str) {
        try {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").replaceAll("đ", "d").replaceAll("Đ", "D");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setOnSelectedItemInMACT(MultiAutoCompleteTextView actvHobby){
        actvHobby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(actvHobby.getText().toString().length()>0) {
                    actvHobby.setText(actvHobby.getText().toString().substring(0, actvHobby.getText().toString().lastIndexOf(",")) + convertStringToNFD((String) parent.getItemAtPosition(position)));
                }
                else {
                    actvHobby.setText(actvHobby.getText().toString().substring(0, actvHobby.getText().toString().lastIndexOf(","))+"," + convertStringToNFD((String) parent.getItemAtPosition(position)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
