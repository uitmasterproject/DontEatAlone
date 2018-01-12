package com.app.donteatalone.views.register;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.ImageProcessor;
import com.app.donteatalone.views.main.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.StringEscapeUtils;

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
    private StorageReference storageRefImage;
    private ImageView imgWomanAvatar, imgManAvatar, imgAvatar;
    private RelativeLayout rltWomanAvatar, rltManAvatar, rltAvatar;
    private Button btnNext;
    private String gender;
    private ViewPager _mViewPager;
    private TextView tvTutorial, tvTitile;
    private LinearLayout llRoot;
    private boolean isChosen = false;

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
        tvTitile = (TextView) view.findViewById(R.id.fragment_register_step3_tv_title);
        llRoot = (LinearLayout) view.findViewById(R.id.fragment_register_step3_ll_root);
        rltAvatar.setVisibility(View.GONE);
    }

    /*Hide softkeyboard when touch outsite edittext*/
    private void llRootTouch() {
        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.touchOutsideHideSoftKeyboard(getActivity());
            }
        });
    }

    private void setClickGender() {
        imgManAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationImageAvatar(rltManAvatar, R.drawable.avatar_man);
                gender = getString(R.string.man);
                tvTitile.setText(getString(R.string.register_step_3_title_2));
                if (tvTutorial.getText().toString().equals(getResources().getString(R.string.empty_gender))) {
                    tvTutorial.setText("");
                } else tvTutorial.setText(getString(R.string.register_step_3_suggest_2));
            }
        });
        imgWomanAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationImageAvatar(rltWomanAvatar, R.drawable.avatar_woman);
                gender = getString(R.string.woman);
                tvTitile.setText(getString(R.string.register_step_3_title_2));
                if (tvTutorial.getText().toString().equals(getResources().getString(R.string.empty_gender))) {
                    tvTutorial.setText("");
                } else tvTutorial.setText(getString(R.string.register_step_3_suggest_2));
            }
        });
    }

    private void animationImageAvatar(RelativeLayout re, int sourceImg) {
        rltManAvatar.setVisibility(View.GONE);
        rltWomanAvatar.setVisibility(View.GONE);
        rltAvatar.setVisibility(View.VISIBLE);
        imgAvatar.setImageResource(sourceImg);
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

        builder.setTitle("Thêm Ảnh");
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
        isChosen = true;
        ImageProcessor.activityImageResult(resultCode, requestCode, data, RegisterStep3Fragment.this, null, imgAvatar);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ImageProcessor.requestPermissionsResult(requestCode, grantResults[0], RegisterStep3Fragment.this, null);

    }

    private void clickButtonNextStep() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gender == null) {
                    tvTutorial.setText(getResources().getString(R.string.empty_gender));
                    tvTutorial.setTextColor(ContextCompat.getColor(getContext(), R.color.color_orange_pressed));
                } else {
                    if (isChosen) {
                        if (AppUtils.isNetworkAvailable(getActivity())) {
                            storageRefImage = FirebaseStorage.getInstance().getReferenceFromUrl(MainActivity.URL_STORAGE_FIRE_BASE).
                                    child(MainActivity.REGISTER_PATH_STORAGE_FIRE_BASE + userName.getPhone());
                            UploadTask uploadTask = storageRefImage.putBytes(ImageProcessor.convertBitmapToByte(((BitmapDrawable) imgAvatar.getDrawable()).getBitmap()));
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    @SuppressWarnings("VisibleForTests")
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    if (downloadUrl != null) {
                                        userName.setAvatar(downloadUrl.toString());
                                    } else {
                                        userName.setAvatar(null);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (gender.equals(R.string.woman)) {
                            userName.setAvatar("https://firebasestorage.googleapis.com/v0/b/dont-eat-alone-storage.appspot.com/o/avatar_woman.png?alt=media&token=7ada04ec-abef-4224-81a2-0ba209f11cef");
                        } else {
                            userName.setAvatar("https://firebasestorage.googleapis.com/v0/b/dont-eat-alone-storage.appspot.com/o/avatar_man.png?alt=media&token=3f4db113-759c-4819-9998-8400687af0d8");
                        }
                    }
                    userName.setGender(StringEscapeUtils.escapeJava(gender));
                    _mViewPager.setCurrentItem(3, true);
                }
            }
        });
    }


}
