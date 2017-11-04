package com.app.donteatalone.views.main.blog;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.donteatalone.R;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.utils.AppUtils;
import com.app.donteatalone.utils.ExifUtil;
import com.app.donteatalone.views.main.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.app.donteatalone.utils.AppUtils.decodeBitmap;
import static com.app.donteatalone.views.main.blog.DetailBlogActivity.ARG_ITEM_BLOG;

/**
 * Created by ChomChom on 4/14/2017
 */

public class StatusActivity extends Activity {

    private LinearLayout llContainer;
    private TextView txtTitle;
    private ImageButton iBtnCamera, iBtnPhoto, iBtnFelling;
    private Bitmap bitmap;
    private EditText edtStatus;
    private TextView txtCancel, txtSave;
    private Spinner snLimit;
    private InfoBlog infoBlog;
    private int resourceIcon;

    private PopupWindow popupMenu;
    private View view;

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
        txtCancel = (TextView) findViewById(R.id.activity_blog_write_status_btn_cancel);
        txtTitle = (TextView) findViewById(R.id.activity_blog_write_status_btn_title);
        txtSave = (TextView) findViewById(R.id.activity_blog_write_status_btn_save);
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

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_menu_icon, null);

        RecyclerView listMenuIcon = (RecyclerView) view.findViewById(R.id.list_menu_icon);
        listMenuIcon.setLayoutManager(new LinearLayoutManager(StatusActivity.this, LinearLayoutManager.HORIZONTAL, false));

        PopupIconAdapter iconAdapter = new PopupIconAdapter(StatusActivity.this);

        listMenuIcon.setAdapter(iconAdapter);

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        popupMenu = new PopupWindow(view, point.x, 70);
        popupMenu.setFocusable(true);
        popupMenu.setOutsideTouchable(true);
        popupMenu.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(StatusActivity.this, R.color.black_anpha_40)));

        iconAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int resource) {
                resourceIcon = resource;
                iBtnFelling.setImageResource(resource);
                popupMenu.dismiss();
            }
        });
    }

    private void setDataEdit() {
        if (getIntent().getParcelableExtra(ARG_ITEM_BLOG) != null) {
            infoBlog = getIntent().getParcelableExtra(ARG_ITEM_BLOG);
            txtTitle.setText("Edit Blog");
            txtSave.setText("Edit");
            if (infoBlog.getLimit().equals(getResources().getStringArray(R.array.limit)[1])) {
                snLimit.setSelection(1);
            } else {
                snLimit.setSelection(0);
            }
            if (infoBlog.getFeeling() != 0) {
                iBtnFelling.setImageResource(infoBlog.getFeeling());
            }
            llContainer.removeAllViews();
            setValueContent();
        }
    }

    private void setValueContent() {
        String str = infoBlog.getInfoStatus();
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
                Bitmap bitmap = decodeBitmap(str.substring(7, index));
                imgImage.setLayoutParams(new LinearLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight()));
                imgImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imgImage.setImageBitmap(bitmap);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imgImage.getLayoutParams();
                layoutParams.setMargins(10, 10, 20, 10);
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                imgImage.setLayoutParams(layoutParams);

                llContainer.addView(imgImage);

                str = str.substring(index + 8);
            }
            setClickImageContent();
        }
    }

    private void clickCancel() {
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatusActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clickPost() {

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAsDialog saveAsDialog;
                if (infoBlog != null) {
                    saveAsDialog = new SaveAsDialog(StatusActivity.this, setInfoStatus(), true);

                } else {
                    saveAsDialog = new SaveAsDialog(StatusActivity.this, setInfoStatus(), false);
                }
                saveAsDialog.showDialog();
            }
        });
    }

    private InfoBlog setInfoStatus() {
        String thisDate;
        if (infoBlog == null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss", Locale.ENGLISH);
            Date date = new Date();
            thisDate = dateFormat.format(date);
        } else {
            thisDate = infoBlog.getDate();
        }
        ArrayList<String> image = new ArrayList<>();

        String content = "";
        for (int count = 0; count < llContainer.getChildCount(); count++) {
            if (llContainer.getChildAt(count).getClass().getName().equals("android.widget.EditText")) {
                EditText editText = (EditText) llContainer.getChildAt(count);
                content += "<text>" + editText.getText().toString() + "</text>";
            } else if (llContainer.getChildAt(count).getClass().getName().equals("android.widget.ImageView")) {
                ImageView imageView = (ImageView) llContainer.getChildAt(count);
                if (((BitmapDrawable) imageView.getDrawable()).getBitmap() != null) {
                    content += "<image>" + AppUtils.convertBitmaptoString(((BitmapDrawable) imageView.getDrawable()).getBitmap()) + "</image>";
                    image.add(AppUtils.convertBitmaptoString(((BitmapDrawable) imageView.getDrawable()).getBitmap()));
                }
            }
        }
        return new InfoBlog(thisDate, content, resourceIcon, image, snLimit.getSelectedItem().toString());
    }

    private void clickIBtnCamera() {
        iBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(intent, 1);
            }
        });
    }

    private void clickIBtnPhoto() {
        iBtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
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
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    String imagePath = f.getAbsolutePath();
                    Bitmap loadedBitmap = BitmapFactory.decodeFile(imagePath);

                    bitmap = ExifUtil.definiteRotate(imagePath, loadedBitmap);
                    bitmap = ExifUtil.scaleBitmap(bitmap, StatusActivity.this);
                    if (bitmap != null) {
                        setElementImageForLinearLayout(bitmap, 1);
                    }
                    String path = android.os.Environment.getExternalStorageDirectory().toString();
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
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
                final Uri imageUri = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);

                    bitmap = ExifUtil.definiteRotate(picturePath, loadedBitmap);
                }
                bitmap = ExifUtil.scaleBitmap(bitmap, StatusActivity.this);
                if (bitmap != null) {
                    setElementImageForLinearLayout(bitmap, 1);
                }
            } else {
                final Uri imageUri = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);

                    bitmap = ExifUtil.definiteRotate(picturePath, loadedBitmap);
                }
                bitmap = ExifUtil.scaleBitmap(bitmap, StatusActivity.this);
                if (bitmap != null) {
                    setElementImageForLinearLayout(bitmap, requestCode);
                }
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
        imgAddPhotoContent.setLayoutParams(new LinearLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight()));
        imgAddPhotoContent.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgAddPhotoContent.setImageBitmap(bitmap);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imgAddPhotoContent.getLayoutParams();
        layoutParams.setMargins(10, 10, 20, 10);
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
                int finalCount = count;
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
