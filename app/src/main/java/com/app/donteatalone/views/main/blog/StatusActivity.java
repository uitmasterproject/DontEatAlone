package com.app.donteatalone.views.main.blog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Felling;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.utils.ExifUtil;
import com.app.donteatalone.views.main.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 4/14/2017.
 */

public class StatusActivity extends Activity {

    private ImageView imgAvatar, imgAddFeelingIcon;
    private LinearLayout llcontainerFeeling, llContainer;
    private TextView txtNameUser, txtAddFeeling, txtTitle;
    private ImageButton imgbtnCamera, imgbtnPhoto, imgbtnFelling, imgbtnAction;
    private Bitmap bitmap;
    private EditText edtStatus, edtTitle;
    private TextView txtCancel, txtPost;
    private Spinner snLimit;
    private String phone;
    private InfoBlog infoBlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_write_status);
        init();
        setAvatarforimgAvatar();
        setDataEdit();
        clickimgbtnCamera();
        clickimgbtnPhoto();
        clickimgbtnFelling();
        clickbtnCancel();
        clickbtnPost();
    }


    private void init() {
        txtCancel = (TextView) findViewById(R.id.activity_blog_write_status_btn_cancel);
        txtPost = (TextView) findViewById(R.id.activity_blog_write_status_btn_post);
        txtTitle = (TextView) findViewById(R.id.activity_blog_write_status_txt_title);
        edtStatus = (EditText) findViewById(R.id.activity_blog_write_status_edt_status);
        edtStatus.requestFocus();
        edtTitle = (EditText) findViewById(R.id.activity_blog_write_staus_edt_title);
        imgAvatar = (ImageView) findViewById(R.id.activity_blog_write_status_img_avatar);
        imgAddFeelingIcon = (ImageView) findViewById(R.id.activity_blog_write_status_img_add_icon_feeling);
        txtNameUser = (TextView) findViewById(R.id.activity_blog_write_status_txt_nameuser);
        txtAddFeeling = (TextView) findViewById(R.id.activity_blog_write_status_txt_add_feeling);
        imgbtnCamera = (ImageButton) findViewById(R.id.activity_blog_write_status_imgbtn_camera);
        imgbtnPhoto = (ImageButton) findViewById(R.id.activity_blog_write_status_imgbtn_photo);
        imgbtnFelling = (ImageButton) findViewById(R.id.activity_blog_write_status_imgbtn_felling);
        imgbtnAction = (ImageButton) findViewById(R.id.activity_blog_write_status_imgbtn_action);
        llcontainerFeeling = (LinearLayout) findViewById(R.id.activity_blog_write_status_ll_container_feeling);
        llcontainerFeeling.setVisibility(View.GONE);
        llContainer = (LinearLayout) findViewById(R.id.activity_blog_write_status_ll_container);
        snLimit = (Spinner) findViewById(R.id.activity_blog_write_status_sn_limit);
        CustomAdapterSpinnerLimit adapter = new CustomAdapterSpinnerLimit(this, android.R.layout.simple_spinner_dropdown_item,
                new ArrayList(Arrays.asList(getResources().getStringArray(R.array.limit))));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snLimit.setAdapter(adapter);

    }

    private void setDataEdit() {
        if (getIntent().getParcelableExtra("infoBlog") != null) {
            infoBlog = getIntent().getParcelableExtra("infoBlog");
            txtTitle.setText("EDIT BLOG");
            txtPost.setText("EDIT");
            imgAddFeelingIcon.setImageResource(defineIconforFeeling(infoBlog.getFeeling()));
            txtAddFeeling.setText(infoBlog.getFeeling());
            if (infoBlog.getLimit().equals("private")) {
                snLimit.setSelection(0);
            } else {
                snLimit.setSelection(1);
            }
            edtTitle.setText(infoBlog.getTitle());
            llContainer.removeAllViews();
            setValueContent();
        }
    }

    private void setValueContent() {
        String str = infoBlog.getInfoStatus();
        while (str.length() != 0) {
            if (str.startsWith("<text>") == true) {
                EditText editText = new EditText(this);
                int index = str.indexOf("</text>");
                editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                editText.setTextColor(getResources().getColor(R.color.black));
                editText.setText(str.substring(6, index));
                llContainer.addView(editText);
                str = str.substring(index + 7);
            } else if (str.startsWith("<image>") == true) {
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

    private Bitmap decodeBitmap(String avatar) {
        Bitmap bitmap = null;
        if (avatar.equals("") != true) {
            try {
                byte[] encodeByte = Base64.decode(avatar, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return bitmap;
    }

    private void clickbtnCancel() {
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatusActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clickbtnPost() {
        txtPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect connect = new Connect();
                Call<Status> modifiedInfoBlog;
                if (infoBlog == null) {
                    modifiedInfoBlog = connect.getRetrofit().addStatusBlog(setInfoStatus(), phone);
                } else {
                    modifiedInfoBlog = connect.getRetrofit().editStatusBlog(setInfoStatus(), phone);
                }
                modifiedInfoBlog.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        if (response != null) {
                            if (response.body().getStatus().equals("Insert success") == true) {
                                Intent intent = new Intent(StatusActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(StatusActivity.this, "check internet again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {

                    }
                });
            }
        });
    }

    private InfoBlog setInfoStatus() {
        String thisDate = "";
        if (infoBlog == null) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            thisDate = dateFormat.format(date);
        } else {
            thisDate = infoBlog.getDate();
        }

        String feeling = txtAddFeeling.getText().toString();
        ArrayList<String> image = new ArrayList<>();

        String content = "";
        for (int count = 0; count < llContainer.getChildCount(); count++) {
            if (llContainer.getChildAt(count).getClass().getName().equals("android.widget.EditText") == true) {
                EditText editText = (EditText) llContainer.getChildAt(count);
                content += "<text>" + editText.getText().toString() + "</text>";
            } else if(llContainer.getChildAt(count).getClass().getName().equals("android.widget.ImageView")==true) {
                ImageView imageView = (ImageView) llContainer.getChildAt(count);
                content += "<image>" + convertBitmaptoString(((BitmapDrawable) imageView.getDrawable()).getBitmap()) + "</image>";
                image.add(convertBitmaptoString(((BitmapDrawable) imageView.getDrawable()).getBitmap()));
            }
        }

        InfoBlog infoBlog = new InfoBlog(edtTitle.getText().toString(), thisDate, content, feeling, image, snLimit.getSelectedItem().toString());
        return infoBlog;
    }

    private void clickimgbtnCamera() {
        imgbtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(intent, 1);
            }
        });
    }

    private void clickimgbtnPhoto() {
        imgbtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });
    }

    private void clickimgbtnFelling() {
        imgbtnFelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(StatusActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_show_felling);
                initElementDialogShowFelling(dialog);
                dialog.show();
            }
        });
    }

    private void initElementDialogShowFelling(final Dialog dialog) {
        SearchView searchView = (SearchView) dialog.findViewById(R.id.custom_dialog_show_felling_srch_felling);

        GridView gridView = (GridView) dialog.findViewById(R.id.custom_dialog_show_felling_grv_felling);

        final CustomAdapterGridViewShowFelling adapter = new CustomAdapterGridViewShowFelling(dialog.getContext(), cloneDataFelling());
        gridView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setFellingwhenClickFellingICon(position);
                dialog.cancel();
            }
        });
    }

    private void setFellingwhenClickFellingICon(int position) {
        llcontainerFeeling.setVisibility(View.VISIBLE);
        txtAddFeeling.setText("feeling " + cloneDataFelling().get(position).getFelling());
        imgAddFeelingIcon.setImageResource(cloneDataFelling().get(position).getResourceIcon());
    }

    private ArrayList<Felling> cloneDataFelling() {
        ArrayList<Felling> fellings = new ArrayList<>();
        fellings.add(new Felling(R.drawable.ic_felling_cute, "đáng yêu"));
        fellings.add(new Felling(R.drawable.ic_felling_exciting, "thú vị"));
        fellings.add(new Felling(R.drawable.ic_felling_smile, "vui vẻ"));
        fellings.add(new Felling(R.drawable.ic_felling_sad, "buồn"));
        return fellings;
    }

    private int defineIconforFeeling(String feeling) {
        int icon = 0;
        if (feeling.equals("feeling đáng yêu") == true) {
            icon = R.drawable.ic_felling_cute;
            return icon;
        } else if (feeling.equals("feeling thú vị") == true) {
            icon = R.drawable.ic_felling_exciting;
            return icon;
        } else if (feeling.equals("feeling vui vẻ") == true) {
            icon = R.drawable.ic_felling_smile;
            return icon;
        } else if (feeling.equals("feeling buồn") == true) {
            icon = R.drawable.ic_felling_sad;
            return icon;
        }
        return icon;
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
                        setElementImageforLinearLayout(bitmap, 1);
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
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);

                bitmap = ExifUtil.definiteRotate(picturePath, loadedBitmap);
                bitmap = ExifUtil.scaleBitmap(bitmap, StatusActivity.this);
                if (bitmap != null) {
                    setElementImageforLinearLayout(bitmap, 1);
                }
            } else {
                final Uri imageUri = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);

                bitmap = ExifUtil.definiteRotate(picturePath, loadedBitmap);
                bitmap = ExifUtil.scaleBitmap(bitmap, StatusActivity.this);
                if (bitmap != null) {
                    setElementImageforLinearLayout(bitmap, requestCode);
                }
            }

        }

    }


    //add image dynamic when click add image
    //long click image to delete.
    //1. get element in linear.
    //2. give this view.
    //3. thisview.set action.
    private void setElementImageforLinearLayout(Bitmap bitmap, int check) {
        if (check == 1) {
            if (llContainer.getChildAt(llContainer.getChildCount() - 1).getClass().getName().equals("android.widget.EditText") == true &&
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
                        new CustomDialogSelectOption(StatusActivity.this).showDialog(dynamicImage, finalCount, llContainer);
                        return false;
                    }
                });
            }
        }
    }


    private String storeReference() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        Boolean bchk = sharedPreferences.getBoolean("checked", false);
        String avatar = "";
        if (bchk == false) {
            avatar = sharedPreferences.getString("avatarLogin", "");
            txtNameUser.setText(sharedPreferences.getString("fullnameLogin", "").toUpperCase());
            phone = sharedPreferences.getString("phoneLogin", "");
        }
        return avatar;
    }

    private String convertBitmaptoString(Bitmap bm) {
        String tempConvert = "";
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, arrayOutputStream);
        byte[] b = arrayOutputStream.toByteArray();
        tempConvert = Base64.encodeToString(b, Base64.DEFAULT);
        return tempConvert;
    }

    private void setAvatarforimgAvatar() {
        Bitmap bitmap = decodeBitmap(storeReference());
        if (bitmap != null) {
            imgAvatar.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.animator.animator_bottom_up, R.animator.stay);
    }


}
