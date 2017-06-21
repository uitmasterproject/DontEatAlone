package com.app.donteatalone.views.main.blog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.donteatalone.R;
import com.app.donteatalone.connectmongo.Connect;
import com.app.donteatalone.model.Felling;
import com.app.donteatalone.model.InfoBlog;
import com.app.donteatalone.model.Status;
import com.app.donteatalone.views.main.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ChomChom on 4/14/2017.
 */

public class StatusActivity extends Activity {

    private ImageView imgAvatar,imgAddPhotoContent, imgAddFeelingIcon;
    private LinearLayout llcontainerImage, llcontainerFeeling;
    private TextView txtNameUser, txtAddFeeling;
    private ImageButton imgbtnCamera, imgbtnPhoto, imgbtnFelling, imgbtnAction;
    private Bitmap bitmap;
    private EditText edtStatus;
    private HorizontalScrollView hsvcontainerImage;
    private Button btnCancel, btnPost;
    private String phone;
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_write_status);
        init();
        setAvatarforimgAvatar();
        clickimgbtnCamera();
        clickimgbtnPhoto();
        clickimgbtnFelling();
        clickbtnCancel();
        clickbtnPost();
    }



    private void init(){
        btnCancel=(Button) findViewById(R.id.activity_blog_write_status_btn_cancel);
        btnPost=(Button) findViewById(R.id.activity_blog_write_status_btn_post);
        edtStatus=(EditText) findViewById(R.id.activity_blog_write_status_edt_status);
        imgAvatar=(ImageView) findViewById(R.id.activity_blog_write_status_img_avatar);
        imgAddFeelingIcon=(ImageView) findViewById(R.id.activity_blog_write_status_img_add_icon_feeling);
        txtNameUser=(TextView) findViewById(R.id.activity_blog_write_status_txt_nameuser);
        txtAddFeeling=(TextView) findViewById(R.id.activity_blog_write_status_txt_add_feeling);
        imgbtnCamera=(ImageButton) findViewById(R.id.activity_blog_write_status_imgbtn_camera);
        imgbtnPhoto=(ImageButton) findViewById(R.id.activity_blog_write_status_imgbtn_photo);
        imgbtnFelling=(ImageButton) findViewById(R.id.activity_blog_write_status_imgbtn_felling);
        imgbtnAction=(ImageButton) findViewById(R.id.activity_blog_write_status_imgbtn_action);
        hsvcontainerImage=(HorizontalScrollView) findViewById(R.id.activity_blog_write_status_hsv_container_image);
        hsvcontainerImage.setVisibility(View.GONE);
        llcontainerImage=(LinearLayout) findViewById(R.id.activity_blog_write_status_ll_container_image);
        llcontainerFeeling=(LinearLayout) findViewById(R.id.activity_blog_write_status_ll_container_feeling);
        llcontainerFeeling.setVisibility(View.GONE);
    }

    private void clickbtnCancel(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatusActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void clickbtnPost(){
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect connect=new Connect();
                Call<Status> addInfoBlog= connect.getRetrofit().addStatusBlog(setInfoStatus(),phone);
                addInfoBlog.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        if(response.body().getStatus().equals("Insert success")==true){
                            Intent intent = new Intent(StatusActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(StatusActivity.this,"check internet again",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {

                    }
                });
            }
        });
    }

    private InfoBlog setInfoStatus(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String thisDate=dateFormat.format(date);
        String status=edtStatus.getText().toString();
        String feeling= txtAddFeeling.getText().toString();
        ArrayList<String> image= giveImageAddinStatus();
        InfoBlog infoBlog = new InfoBlog(thisDate,status,feeling,image);
        return infoBlog;
    }

    private ArrayList<String> giveImageAddinStatus(){
        ImageView v;
        ArrayList<String> covertImage = new ArrayList<>();
        for(int j=0;j<llcontainerImage.getChildCount();j++){
            v=(ImageView) llcontainerImage.getChildAt(j);
            Bitmap bm=((BitmapDrawable)v.getDrawable()).getBitmap();
            if(bm!=null) {
                if(bm.getHeight()< bm.getWidth()) {
                    bm = Bitmap.createScaledBitmap(bm, bm.getWidth() * 100 / bm.getHeight(), 100, true);
                }
                else {
                    bm = Bitmap.createScaledBitmap(bm, 100, bm.getHeight() * 100 / bm.getWidth(), true);
                }
                covertImage.add(convertBitmaptoString(bm));
            }
        }
        return covertImage;
    }

    private void clickimgbtnCamera(){
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

    private void clickimgbtnPhoto(){
        imgbtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });
    }

    private void clickimgbtnFelling(){
        imgbtnFelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(StatusActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_show_felling);
                initElementDialogShowFelling(dialog);
                dialog.show();
            }
        });
    }

    private void initElementDialogShowFelling(final Dialog dialog){
        SearchView searchView=(SearchView) dialog.findViewById(R.id.custom_dialog_show_felling_srch_felling);

        GridView gridView=(GridView)dialog.findViewById(R.id.custom_dialog_show_felling_grv_felling);

        final CustomAdapterGridViewShowFelling adapter= new CustomAdapterGridViewShowFelling(dialog.getContext(),cloneDataFelling());
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

    private void setFellingwhenClickFellingICon(int position){
        llcontainerFeeling.setVisibility(View.VISIBLE);
        txtAddFeeling.setText("feeling "+cloneDataFelling().get(position).getFelling());
        imgAddFeelingIcon.setImageResource(cloneDataFelling().get(position).getResourceIcon());
    }

    private ArrayList<Felling> cloneDataFelling(){
        ArrayList<Felling> fellings=new ArrayList<>();
        fellings.add(new Felling(R.drawable.ic_felling_cute,"đáng yêu"));
        fellings.add(new Felling(R.drawable.ic_felling_exciting,"thú vị"));
        fellings.add(new Felling(R.drawable.ic_felling_smile,"vui vẻ"));
        fellings.add(new Felling(R.drawable.ic_felling_sad,"buồn"));
        return fellings;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                i++;
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
                    bitmap=tempbitmap;
                    if(bitmap!=null){
                        setElementImageforLinearLayout(bitmap);
                    }
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
                i++;
                final Uri imageUri = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap tempbitmap = BitmapFactory.decodeStream(imageStream);
                tempbitmap = Bitmap.createScaledBitmap(tempbitmap, 90, 90, true);
                bitmap = tempbitmap;
                imgAvatar.setImageBitmap(tempbitmap);
                if(bitmap!=null){
                    setElementImageforLinearLayout(bitmap);
                }
            }

        }

    }


    //add image dynamic when click add image
    //long click image to delete.
    //1. get element in linear.
    //2. give this view.
    //3. thisview.set action.
    private void setElementImageforLinearLayout(Bitmap bitmap){
        if(i==1){
            LinearLayout.LayoutParams paramsedtStatus = (LinearLayout.LayoutParams)edtStatus.getLayoutParams();
            paramsedtStatus.weight = 1.0f;
            edtStatus.setLayoutParams(paramsedtStatus);
            hsvcontainerImage.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams paramsllcontainerImage = (LinearLayout.LayoutParams)hsvcontainerImage.getLayoutParams();
            paramsllcontainerImage.weight = 1.0f;
            hsvcontainerImage.setLayoutParams(paramsllcontainerImage);
        }
        imgAddPhotoContent=new ImageView(this);
        imgAddPhotoContent.setLayoutParams(new LinearLayout.LayoutParams((bitmap.getWidth()*300)/bitmap.getHeight(), 300));
        imgAddPhotoContent.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imgAddPhotoContent.setImageBitmap(bitmap);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imgAddPhotoContent.getLayoutParams();
        layoutParams.setMargins(10, 10, 20, 10);
        imgAddPhotoContent.setLayoutParams(layoutParams);
        llcontainerImage.addView(imgAddPhotoContent);

        for(int count=0;count<llcontainerImage.getChildCount();count++){
            final ImageView dynamicImage=(ImageView)llcontainerImage.getChildAt(count);
            dynamicImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dynamicImage.setVisibility(View.GONE);
                    dynamicImage.setImageBitmap(null);
                    dynamicImage.destroyDrawingCache();
                    if(llcontainerImage.getChildCount()==0){
                        hsvcontainerImage.setVisibility(View.GONE);
                    }
                    return false;
                }
            });
        }
    }


    private String storeReference(){
        SharedPreferences sharedPreferences=getSharedPreferences("account",MODE_PRIVATE);
        Boolean bchk=sharedPreferences.getBoolean("checked", false);
        String avatar="";
        if(bchk==false)
        {
            avatar=sharedPreferences.getString("avatarLogin", "");
            txtNameUser.setText(sharedPreferences.getString("fullnameLogin","").toUpperCase());
            phone=sharedPreferences.getString("phoneLogin","");
        }
        return avatar;
    }

    private Bitmap decodeBitmap(){
        String avatar = storeReference();
        Bitmap bitmap=null;
        if(avatar.equals("")!=true) {
            try {
                byte[] encodeByte = Base64.decode(avatar, Base64.DEFAULT);
                 bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return bitmap;
    }

    private String convertBitmaptoString(Bitmap bm){
        String tempConvert="";
        ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,90,arrayOutputStream);
        byte[] b=arrayOutputStream.toByteArray();
        tempConvert= Base64.encodeToString(b,Base64.DEFAULT);
        return tempConvert;
    }

    private void setAvatarforimgAvatar(){
        Bitmap bitmap=decodeBitmap();
        if(bitmap!=null){
            imgAvatar.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.animator.animator_bottom_up, R.animator.stay);
    }


}
