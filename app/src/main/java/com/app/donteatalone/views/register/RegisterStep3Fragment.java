package com.app.donteatalone.views.register;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;

import static android.app.Activity.RESULT_OK;
import static com.app.donteatalone.views.register.RegisterStep1Fragment.userName;

/**
 * Created by ChomChom on 4/7/2017
 */

public class RegisterStep3Fragment extends Fragment {

    private View viewGroup;
    private ImageView imgWomanAvatar, imgManAvatar, imgAvatar;
    private RelativeLayout rltWomanAvatar, rltManAvatar, rltAvatar;
    private RelativeLayout rlNext, rlClose;
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
        viewGroup = inflater.inflate(R.layout.fragment_register_step3, null);
        init();
        llRootTouch();
        setClickGender();
        setClickAvatar();
        clickButtonNextStep();
        rlCloseClick();
        return viewGroup;
    }

    //Khoi tao gia tri cho cac bien
    private void init() {
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        rltManAvatar = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step3_rlt_man_avatar);
        rltWomanAvatar = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step3_rlt_woman_avatar);
        rltAvatar = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step3_rlt_avatar);
        imgManAvatar = (ImageView) viewGroup.findViewById(R.id.fragment_register_step3_img_avatar_man);
        imgWomanAvatar = (ImageView) viewGroup.findViewById(R.id.fragment_register_step3_img_avatar_woman);
        imgAvatar = (ImageView) viewGroup.findViewById(R.id.fragment_register_step3_img_avatar);
        rlNext = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step3_btn_next);
        tvTutorial = (TextView) viewGroup.findViewById(R.id.fragment_register_step3_tv_tutorial);
        rlClose = (RelativeLayout) viewGroup.findViewById(R.id.fragment_register_step3_close);
        llRoot = (LinearLayout) viewGroup.findViewById(R.id.fragment_register_step3_ll_root);
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

    private void rlCloseClick() {
        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
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
        final CharSequence[] options = {"Take photo", "Choose from Gallery", "Cancel"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);

        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Take photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
        if (data != null) {
            if (resultCode == RESULT_OK) {
                if (requestCode == 1) {
                    performCrop(data.getData());
                } else if (requestCode == 2) {
                    performCrop(data.getData());
                } else if (requestCode == 3) {
                    Bundle extras = data.getExtras();
                    Bitmap thePic = extras.getParcelable("data");
                    imgAvatar.setImageBitmap(thePic);
                }
            }
        }
    }

    private void performCrop(Uri uri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(uri, "image/*");
            cropIntent.putExtra("crop", true);
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, 3);
        } catch (ActivityNotFoundException anfe) {

        }
    }

    private void clickButtonNextStep() {
        rlNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intChosen == -1) {
                    tvTutorial.setText("You have to choose your gender");
                    tvTutorial.setTextColor(ContextCompat.getColor(getContext(), R.color.color_orange_pressed));
                } else {
                    userName.setAvatar(AppUtils.convertBitmaptoString(((BitmapDrawable) imgAvatar.getDrawable()).getBitmap()));
                    userName.setGender(gender);
                    _mViewPager.setCurrentItem(3, true);
                }
            }
        });
    }


}
