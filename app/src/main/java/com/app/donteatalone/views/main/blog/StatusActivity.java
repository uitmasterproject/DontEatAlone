package com.app.donteatalone.views.main.blog;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.base.BaseProgress;
import com.app.donteatalone.base.OnRecyclerItemClickListener;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.utils.ImageProcessor;
import com.app.donteatalone.utils.MySharePreference;
import com.app.donteatalone.views.main.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.app.donteatalone.utils.ImageProcessor.PERMISSION_CAMERA_REQUEST_CODE;
import static com.app.donteatalone.utils.ImageProcessor.PERMISSION_READ_REQUEST_CODE;
import static com.app.donteatalone.utils.ImageProcessor.RESULT_CHOOSE_IMAGE;
import static com.app.donteatalone.utils.ImageProcessor.RESULT_TAKE_PHOTO;
import static com.app.donteatalone.views.main.blog.DetailBlogActivity.ARG_ITEM_BLOG;

/**
 * Created by ChomChom on 4/14/2017
 */

public class StatusActivity extends Activity {

    private LinearLayout llContainer;
    private ImageView imgEmotion;
    private ImageButton iBtnCamera, iBtnPhoto, iBtnFelling;
    private Bitmap bitmap;
    private EditText edtStatus;
    private ImageView imgCancel, imgSave;
    private Spinner snLimit;
    private InfoBlog infoBlog;
    private int resourceIcon;

    private PopupWindow popupMenu;
    private View view;

    private String content;
    private int countResult;
    private int countImage;
    private String thisDate, title;

    private SaveAsDialog saveAsDialog;

    private StorageReference storageRefImage;

    private BaseProgress baseProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_write_status);
        init();
        setDataEdit();
        clickIBtnCamera();
        clickIBtnPhoto();
        clickIBtnFelling();
        clickCancel();
        clickPost();

    }


    private void init() {
        imgCancel = (ImageView) findViewById(R.id.activity_blog_write_status_btn_cancel);
        imgEmotion = (ImageView) findViewById(R.id.activity_blog_write_status_txt_emotion);
        imgSave = (ImageView) findViewById(R.id.activity_blog_write_status_btn_save);
        edtStatus = (EditText) findViewById(R.id.activity_blog_write_status_edt_status);
        edtStatus.requestFocus();
        iBtnCamera = (ImageButton) findViewById(R.id.activity_blog_write_status_imgbtn_camera);
        iBtnPhoto = (ImageButton) findViewById(R.id.activity_blog_write_status_imgbtn_photo);
        iBtnFelling = (ImageButton) findViewById(R.id.activity_blog_write_status_imgbtn_felling);
        llContainer = (LinearLayout) findViewById(R.id.activity_blog_write_status_ll_container);
        snLimit = (Spinner) findViewById(R.id.activity_blog_write_status_sn_limit);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.limit));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snLimit.setAdapter(adapter);

        imgEmotion.setImageResource(R.drawable.ic_happy);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_menu_icon, null);

        RecyclerView listMenuIcon = (RecyclerView) view.findViewById(R.id.list_menu_icon);
        listMenuIcon.setLayoutManager(new LinearLayoutManager(StatusActivity.this, LinearLayoutManager.HORIZONTAL, false));

        PopupIconAdapter iconAdapter = new PopupIconAdapter(StatusActivity.this);

        listMenuIcon.setAdapter(iconAdapter);

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        popupMenu = new PopupWindow(view, point.x, (int) getResources().getDimension(R.dimen.pop_up_size));
        popupMenu.setFocusable(true);
        popupMenu.setOutsideTouchable(true);

        iconAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int resource) {
                resourceIcon = resource;
                iBtnFelling.setImageResource(resource);
                imgEmotion.setImageResource(resource);
                popupMenu.dismiss();
            }
        });
    }

    private void setDataEdit() {
        if (getIntent().getParcelableExtra(ARG_ITEM_BLOG) != null) {
            infoBlog = getIntent().getParcelableExtra(ARG_ITEM_BLOG);
            if (infoBlog.getFeeling() == 0) {
                imgEmotion.setImageResource(R.drawable.ic_happy);
            } else {
                imgEmotion.setImageResource(infoBlog.getFeeling());
            }
            imgSave.setImageResource(R.drawable.ic_save);
            if (infoBlog.getLimit().equals(getResources().getStringArray(R.array.limit)[1])) {
                snLimit.setSelection(1);
            } else {
                snLimit.setSelection(0);
            }
            if (infoBlog.getFeeling() != 0) {
                try {
                    iBtnFelling.setImageResource(infoBlog.getFeeling());
                }catch (Exception e){
                    iBtnFelling.setImageResource(R.drawable.ic_happy);
                }

            }
            llContainer.removeAllViews();
            setValueContent();
        }
    }

    private void setValueContent() {
        String str = infoBlog.getInfoStatus();
        int count = 0;
        while (str.length() != 0) {

            if (str.startsWith("<text>")) {

                EditText editText = new EditText(this);
                int index = str.indexOf("</text>");
                editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                editText.setTextColor(ContextCompat.getColor(StatusActivity.this, R.color.black));
                editText.setText(str.substring(6, index));
                llContainer.addView(editText);
                str = str.substring(index + 7);

            } else if (str.startsWith("<image>")) {

                ImageView imgImage = new ImageView(this);
                int index = str.indexOf("</image>");

                DisplayMetrics screen = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(screen);
                imgImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Picasso.with(StatusActivity.this)
                        .load(infoBlog.getImage().get(count))
                        .into(imgImage);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imgImage.getLayoutParams();
                layoutParams.setMargins(0, 10, 0, 10);
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                imgImage.setLayoutParams(layoutParams);

                llContainer.addView(imgImage);

                str = str.substring(index + 8);

                count += 1;
            }
            setClickImageContent();
        }
    }

    private void clickCancel() {
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                onBackPressed();
            }
        });
    }

    private void clickPost() {

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseProgress = new BaseProgress();
                baseProgress.showProgressLoading(StatusActivity.this);
                content = "";

                if (infoBlog == null) {
                    DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
                    Date date = new Date();
                    thisDate = dateFormat.format(date);
                } else {
                    title = infoBlog.getTitle();
                    thisDate = infoBlog.getDate();
                }
                final ArrayList<String> image = new ArrayList<>();

                countImage = 0;
                countResult = 0;

                while (countImage < llContainer.getChildCount()) {
                    if (llContainer.getChildAt(countImage).getClass().getName().equals("android.widget.ImageView")) {
                        countResult += 1;
                        break;
                    }
                    countImage += 1;
                }

                if (countResult != 0) {

                    countImage = 0;
                    countResult = 0;

                    for (int count = 0; count < llContainer.getChildCount(); count++) {
                        if (llContainer.getChildAt(count).getClass().getName().equals("android.widget.EditText")) {
                            EditText editText = (EditText) llContainer.getChildAt(count);
                            content += "<text>" + editText.getText().toString() + "</text>";
                        } else if (llContainer.getChildAt(count).getClass().getName().equals("android.widget.ImageView")) {
                            ImageView imageView = (ImageView) llContainer.getChildAt(count);
                            if (((BitmapDrawable) imageView.getDrawable()).getBitmap() != null) {

                                content += "<image>" + countImage + "</image>";

                                countImage += 1;

                                storageRefImage = FirebaseStorage.getInstance().getReferenceFromUrl(MainActivity.URL_STORAGE_FIRE_BASE).
                                        child(MainActivity.BLOG_PATH_STORAGE_FIRE_BASE + new MySharePreference(StatusActivity.this).getPhoneLogin()
                                                + "/"
                                                + thisDate
                                                + "/"
                                                + countImage);

                                storageRefImage.putBytes(ImageProcessor.convertBitmapToByte(((BitmapDrawable) imageView.getDrawable()).getBitmap()))
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                @SuppressWarnings("VisibleForTests")
                                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                                if (downloadUrl != null) {
                                                    image.add(countResult,downloadUrl.toString());
                                                    countResult += 1;
                                                }

                                                if (countResult == countImage) {
                                                    if (infoBlog != null) {
                                                        saveAsDialog = new SaveAsDialog(StatusActivity.this,
                                                                new InfoBlog(title, thisDate, content, resourceIcon, image, snLimit.getSelectedItem().toString()),
                                                                true);

                                                    } else {
                                                        saveAsDialog = new SaveAsDialog(StatusActivity.this,
                                                                new InfoBlog(title, thisDate, content, resourceIcon, image, snLimit.getSelectedItem().toString()),
                                                                false);
                                                    }
                                                    baseProgress.hideProgressLoading();
                                                    saveAsDialog.showDialog();
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(StatusActivity.this, getString(R.string.not_save), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                } else {
                    for (int count = 0; count < llContainer.getChildCount(); count++) {
                        if (llContainer.getChildAt(count).getClass().getName().equals("android.widget.EditText")) {
                            EditText editText = (EditText) llContainer.getChildAt(count);
                            content += "<text>" + editText.getText().toString() + "</text>";
                        }
                    }

                    if (infoBlog != null) {
                        saveAsDialog = new SaveAsDialog(StatusActivity.this,
                                new InfoBlog(title, thisDate, content, resourceIcon, image, snLimit.getSelectedItem().toString()),
                                true);

                    } else {
                        saveAsDialog = new SaveAsDialog(StatusActivity.this,
                                new InfoBlog(title, thisDate, content, resourceIcon, image, snLimit.getSelectedItem().toString()),
                                false);
                    }
                    baseProgress.hideProgressLoading();
                    saveAsDialog.showDialog();
                }
            }
        });
    }

    private void clickIBtnCamera() {
        iBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(StatusActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= 23) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
                    }
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, RESULT_TAKE_PHOTO);
                }
            }
        });
    }

    private void clickIBtnPhoto() {
        iBtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(StatusActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= 23) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_REQUEST_CODE);
                    }
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, RESULT_CHOOSE_IMAGE);
                }
            }
        });
    }

    private void clickIBtnFelling() {
        iBtnFelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.showAsDropDown(v);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_TAKE_PHOTO) {

                if (data.getData() != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    bitmap = ImageProcessor.definiteRotate(data.getData().getPath(), bitmap);
                    bitmap = ImageProcessor.scaleBitmap(bitmap, StatusActivity.this);
                }
                if (bitmap != null) {
                    setElementImageForLinearLayout(bitmap, 1);
                }
            } else if (requestCode == RESULT_CHOOSE_IMAGE) {
                if (data.getData() != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                        if (cursor == null) {
                            bitmap = ImageProcessor.definiteRotate(data.getData().getPath(), bitmap);
                        } else {
                            cursor.moveToFirst();
                            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                            bitmap = ImageProcessor.definiteRotate(cursor.getString(idx), bitmap);
                            cursor.close();
                        }
                        bitmap = ImageProcessor.scaleBitmap(bitmap, StatusActivity.this);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bitmap != null) {
                    setElementImageForLinearLayout(bitmap, 1);
                }
            } else {
                if (data.getData() != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                        Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                        if (cursor == null) {
                            bitmap = ImageProcessor.definiteRotate(data.getData().getPath(), bitmap);
                        } else {
                            cursor.moveToFirst();
                            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                            bitmap = ImageProcessor.definiteRotate(cursor.getString(idx), bitmap);
                            cursor.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bitmap = ImageProcessor.scaleBitmap(bitmap, StatusActivity.this);
                }
                if (bitmap != null) {
                    setElementImageForLinearLayout(bitmap, requestCode);
                }
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_READ_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_CHOOSE_IMAGE);

            } else {

                Toast.makeText(StatusActivity.this, getResources().getString(R.string.deny_read_storage), Toast.LENGTH_SHORT).show();

            }

        } else if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_TAKE_PHOTO);

            } else {

                Toast.makeText(StatusActivity.this, getResources().getString(R.string.deny_camera), Toast.LENGTH_SHORT).show();

            }
        }

    }


    //add image dynamic when click add image
    //long click image to delete.
    //1. get element in linear.
    //2. give this view.
    //3. thisview.set action.
    private void setElementImageForLinearLayout(Bitmap bitmap, int check) {
        if (check == 1) {
            if (llContainer.getChildAt(llContainer.getChildCount() - 1).getClass().getName().equals("android.widget.EditText") &&
                    ((EditText) llContainer.getChildAt(llContainer.getChildCount() - 1)).getText().toString().trim().length() <= 0) {
                llContainer.removeViewAt(llContainer.getChildCount() - 1);
            }
        }

        ImageView imgAddPhotoContent = new ImageView(this);
        DisplayMetrics screen = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(screen);
        imgAddPhotoContent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imgAddPhotoContent.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgAddPhotoContent.setImageBitmap(bitmap);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imgAddPhotoContent.getLayoutParams();
        layoutParams.setMargins(0, 10, 0, 10);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        imgAddPhotoContent.setLayoutParams(layoutParams);

        if (check == 1) {
            llContainer.addView(imgAddPhotoContent);

            EditText edtAddContentBlog = new EditText(StatusActivity.this);
            edtAddContentBlog.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            edtAddContentBlog.setBackgroundResource(R.drawable.design_blog_edt_write_status);
            edtAddContentBlog.requestFocus();
            edtAddContentBlog.setTextColor(Color.BLACK);

            llContainer.addView(edtAddContentBlog);
        } else {
            llContainer.addView(imgAddPhotoContent, check - 2);
        }

        setClickImageContent();
    }

    private void setClickImageContent() {
        for (int count = 0; count < llContainer.getChildCount(); count++) {
            if (llContainer.getChildAt(count).getClass().getName().equals("android.widget.ImageView")) {
                final ImageView dynamicImage = (ImageView) llContainer.getChildAt(count);
                final int finalCount = count;
                dynamicImage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new SelectOptionDialog(StatusActivity.this).showDialog(dynamicImage, finalCount, llContainer);
                        return false;
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.animator.animator_bottom_up, R.animator.stay);
    }
}
