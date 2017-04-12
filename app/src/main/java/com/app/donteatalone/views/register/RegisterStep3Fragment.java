package com.app.donteatalone.views.register;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.donteatalone.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ChomChom on 4/7/2017.
 */

public class RegisterStep3Fragment extends Fragment {

    private View viewGroup;
    private ImageView imgWomanAvatar, imgManAvatar, imgAvatar;
    private RelativeLayout rltWomanAvatar, rltManAvatar, rltAvatar;
    private Button btnNextStep;
    private Bitmap bitmap;
    private String gender;
    private ViewPager _mViewPager;

    public static Fragment newInstance(Context context) {
        RegisterStep3Fragment f = new RegisterStep3Fragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = inflater.inflate(R.layout.fragment_register_step3,null);
        init();
        setClickGender();
        setClickAvatar();
        clickButtonNextStep();
        return viewGroup;
    }


    //Khoi tao gia tri cho cac bien
    private void init() {
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        rltManAvatar=(RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step3_rlt_man_avatar);
        rltWomanAvatar=(RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step3_rlt_woman_avatar);
        rltAvatar=(RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step3_rlt_avatar);
        imgManAvatar=(ImageView) viewGroup.findViewById(R.id.fragment_register_step3_img_avatar_man);
        imgWomanAvatar=(ImageView) viewGroup.findViewById(R.id.fragment_register_step3_img_avatar_woman);
        imgAvatar=(ImageView) viewGroup.findViewById(R.id.fragment_register_step3_img_avatar);
        btnNextStep = (Button) viewGroup.findViewById(R.id.fragment_register_step3_btn_next);
        btnNextStep.setVisibility(View.GONE);
        rltAvatar.setVisibility(View.GONE);
    }

    private void setClickGender(){
        imgManAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               animationImageAvatar(rltManAvatar,R.drawable.manavatar,120);
                gender="Man";

            }
        });
        imgWomanAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationImageAvatar(rltWomanAvatar,R.drawable.womanavatar,350);
                gender="Woman";
            }
        });

    }


    private void animationImageAvatar(RelativeLayout re,int sourceimg, int px){
        bitmap=BitmapFactory.decodeResource(getResources(), sourceimg);
        rltManAvatar.setVisibility(View.GONE);
        rltWomanAvatar.setVisibility(View.GONE);
        rltAvatar.setVisibility(View.VISIBLE);
        imgAvatar.setImageResource(sourceimg);
        int x=(int)re.getX();
        int y=(int)re.getY();
        ObjectAnimator animX = ObjectAnimator.ofFloat(rltAvatar, "x", x-px);
        ObjectAnimator animY = ObjectAnimator.ofFloat(rltAvatar, "y", y+200);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        animSetXY.start();
    }

    private void setClickAvatar(){
        rltAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
                btnNextStep.setVisibility(View.VISIBLE);

            }
        });
    }


    public void SelectImage(){
        final CharSequence[] options={"Take photo","Choose from Gallery","Cancel"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext(),R.style.MyDialogTheme);

        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Take photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[which].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                } else if (options[which].equals("Cancel"))
                    dialog.dismiss();

            }
        });
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        window.setGravity(Gravity.BOTTOM|Gravity.CENTER);
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.alpha = 0.7f;
        window.setAttributes(layoutParams);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    Bitmap tempbitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),bitmapOptions);
                    tempbitmap=Bitmap.createScaledBitmap(tempbitmap, 90,90, true);
                    bitmap=tempbitmap;
                    imgAvatar.setImageBitmap(tempbitmap);
                    String path = android.os.Environment.getExternalStorageDirectory().toString();
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContext().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap tempbitmap = (BitmapFactory.decodeFile(picturePath));
                tempbitmap=Bitmap.createScaledBitmap(tempbitmap, 90,90, true);
                bitmap=tempbitmap;
                Log.e("bitmap2",bitmap+"-------------------------------------------");
                imgAvatar.setImageBitmap(tempbitmap);
            }

        }

    }


    private void clickButtonNextStep(){
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterStep1Fragment.userName.setAvatar(convertBitmaptoString());
                RegisterStep1Fragment.userName.setGender(gender);
                saveReference();
                _mViewPager.setCurrentItem(3,true);
            }
        });
    }

    private String convertBitmaptoString(){
        String tempConvert="";
        ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,arrayOutputStream);
        byte[] b=arrayOutputStream.toByteArray();
        tempConvert= Base64.encodeToString(b,Base64.DEFAULT);
        return tempConvert;
    }

    private void saveReference(){
        SharedPreferences sharedPreferences=getContext().getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("avatar",convertBitmaptoString());
        editor.putString("gender",gender);
        editor.commit();
    }

}
