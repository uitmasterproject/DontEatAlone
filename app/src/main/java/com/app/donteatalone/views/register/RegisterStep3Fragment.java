package com.app.donteatalone.views.register;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.ImageProcessor;

import static com.app.donteatalone.utils.ImageProcessor.PERMISSION_CAMERA_REQUEST_CODE;
import static com.app.donteatalone.utils.ImageProcessor.PERMISSION_READ_REQUEST_CODE;
import static com.app.donteatalone.utils.ImageProcessor.RESULT_CHOOSE_IMAGE;
import static com.app.donteatalone.utils.ImageProcessor.RESULT_TAKE_PHOTO;
import static com.app.donteatalone.views.register.RegisterStep1Fragment.userName;

/**
 * Created by ChomChom on 4/7/2017
 */

public class RegisterStep3Fragment extends Fragment {

    private View view;
    private ImageView imgWomanAvatar, imgManAvatar, imgAvatar;
    private RelativeLayout rltWomanAvatar, rltManAvatar, rltAvatar;
    private Button btnNext;
    private String gender;
    private ViewPager _mViewPager;
    private TextView tvTutorial;
    private LinearLayout llRoot;
    private int intChosen = -1;

    public static Fragment newInstance() {
        return new RegisterStep3Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_step3, container, false);
        init();
        llRootTouch();
        setClickGender();
        setClickAvatar();
        clickButtonNextStep();
        return view;
    }

    //Khoi tao gia tri cho cac bien
    private void init() {
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        rltManAvatar = (RelativeLayout) view.findViewById(R.id.fragment_register_step3_rlt_man_avatar);
        rltWomanAvatar = (RelativeLayout) view.findViewById(R.id.fragment_register_step3_rlt_woman_avatar);
        rltAvatar = (RelativeLayout) view.findViewById(R.id.fragment_register_step3_rlt_avatar);
        imgManAvatar = (ImageView) view.findViewById(R.id.fragment_register_step3_img_avatar_man);
        imgWomanAvatar = (ImageView) view.findViewById(R.id.fragment_register_step3_img_avatar_woman);
        imgAvatar = (ImageView) view.findViewById(R.id.fragment_register_step3_img_avatar);
        btnNext = (Button) view.findViewById(R.id.fragment_register_step3_btn_next);
        tvTutorial = (TextView) view.findViewById(R.id.fragment_register_step3_tv_tutorial);
        llRoot = (LinearLayout) view.findViewById(R.id.fragment_register_step3_ll_root);
        rltAvatar.setVisibility(View.GONE);
    }

    /*Hide softkeyboard when touch outsite edittext*/
    private void llRootTouch() {
        llRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideSoftKeyboard(getActivity());
                return true;
            }
        });
    }

    private void setClickGender() {
        imgManAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationImageAvatar(rltManAvatar, R.drawable.avatar_man);
                gender = "Male";
                intChosen = 0;
            }
        });
        imgWomanAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationImageAvatar(rltWomanAvatar, R.drawable.avatar_woman);
                gender = "Female";
                intChosen = 0;
            }
        });
    }

    private void animationImageAvatar(RelativeLayout re, int sourceimg) {
        rltManAvatar.setVisibility(View.GONE);
        rltWomanAvatar.setVisibility(View.GONE);
        rltAvatar.setVisibility(View.VISIBLE);
        imgAvatar.setImageResource(sourceimg);
        TranslateAnimation animation = new TranslateAnimation(imgManAvatar.getX(), imgAvatar.getX(), re.getY(), re.getY());
        animation.setDuration(100000);
        animation.setFillAfter(false);
        rltAvatar.startAnimation(animation);
    }


    private void setClickAvatar() {
        rltAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
    }

    public void SelectImage() {
        final CharSequence[] options = getResources().getStringArray(R.array.option);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            if (Build.VERSION.SDK_INT >= 23) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
                            }
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, RESULT_TAKE_PHOTO);
                        }
                        break;
                    case 1:
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            if (Build.VERSION.SDK_INT >= 23) {
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_REQUEST_CODE);
                            }
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent, RESULT_CHOOSE_IMAGE);
                        }
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        if (dialog.getWindow() != null && window != null) {
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
            layoutParams.x = 0;
            layoutParams.y = 0;
            layoutParams.alpha = 0.7f;
            window.setAttributes(layoutParams);
        }
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageProcessor.activityImageResult(resultCode, requestCode, data, RegisterStep3Fragment.this, imgAvatar);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ImageProcessor.requestPermissionsResult(requestCode, grantResults[0], RegisterStep3Fragment.this);

    }

    private void clickButtonNextStep() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intChosen == -1) {
                    tvTutorial.setText("You have to choose your gender");
                    tvTutorial.setTextColor(ContextCompat.getColor(getContext(), R.color.color_orange_pressed));
                } else {
                    userName.setAvatar(ImageProcessor.convertBitmapToString(((BitmapDrawable) imgAvatar.getDrawable()).getBitmap()));
                    userName.setGender(gender);
                    _mViewPager.setCurrentItem(3, true);
                }
            }
        });
    }


}
