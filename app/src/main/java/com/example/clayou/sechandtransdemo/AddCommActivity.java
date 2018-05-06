package com.example.clayou.sechandtransdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddCommActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    private Spinner selCategory;
    private ImageView img;

    private List<String> categoryList;
    private ArrayAdapter<String> adapter;

    private Uri imageUri;
    private File outputImage;

    private EditText inputName;
    private EditText inputPrice;
    private EditText inputDesc;

    private String owner;
    private String filePath;
    private String commodityName;
    private String commodityCategory;
    private double commodityPrice;
    private String commodityDesc;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comm);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        owner = intent.getStringExtra("owner");

        inputName = findViewById(R.id.input_name);
        inputDesc = findViewById(R.id.description);
        inputPrice = findViewById(R.id.input_price);

        selCategory = findViewById(R.id.select_category);
        categoryList = new ArrayList<String>();
        categoryList.add("电子产品");
        categoryList.add("图书");
        categoryList.add("箱包");
        categoryList.add("其他");

        // 为spinner定义适配器
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);

        // 为适配器设置下拉列表下拉时的样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 为spinner绑定数据适配器
        selCategory.setAdapter(adapter);

        // 为spinner绑定监听器
        selCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                commodityCategory = (String) selCategory.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        img = findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopDialog();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_release) {
            release();
        }
        return super.onOptionsItemSelected(item);
    }

    // 弹出底部dialog选择框
    private void showPopDialog() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_normal, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);

        // 设置显示动画
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        // 设置点击外围取消
        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.show();

        Button btn_gallery = bottomDialog.getWindow().findViewById(R.id.btn_gallery);
        Button btn_photo = bottomDialog.getWindow().findViewById(R.id.btn_photo);
        Button btn_cancel = bottomDialog.getWindow().findViewById(R.id.btn_cancel);


        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InlinedApi")
            @Override
            public void onClick(View view) {
                selectPicInGallery();
                bottomDialog.dismiss();
            }
        });

        btn_photo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InlinedApi")
            @Override
            public void onClick(View view) {
                takePhoto();
                bottomDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
            }
        });
    }

    // 调用相册
    public void selectPicInGallery(){
        if (ContextCompat.checkSelfPermission(AddCommActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddCommActivity.this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {
            openGallery();
        }
    }

    // 打开相册
    private void openGallery(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    // 调用摄像头拍照
    public void takePhoto(){
        // 创建File对象，用于存储拍照后的图片
        outputImage = new File(getExternalCacheDir(), System.currentTimeMillis()+".jpg");
        try{
            filePath = outputImage.getCanonicalPath();
        }catch (IOException e){
            e.printStackTrace();
        }

//        outputImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
//                        + "/test/" + System.currentTimeMillis() + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(AddCommActivity.this,
                    "com.example.clayou.sechandtransdemo.fileprovider", outputImage);
        }else {
            imageUri = Uri.fromFile(outputImage);
        }
        // 启动相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
//                    try{
//                        // 将拍摄的照片显示出来
//                        Log.d("filepath", "onActivityResult: "+imageUri);
////                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
////                        img.setImageBitmap(bitmap);
//
////                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
////                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
////                        commodityImage = byteArrayOutputStream.toByteArray();
//
//                    }catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }

                    //filePath = getRealFilePath(this,imageUri);

//                    Log.d("filepath", "onActivityResult: "+imageUri);
//                    Log.d("filepath", "onActivityResult: "+filePath);
//
//                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//                    img.setImageBitmap(bitmap);

                    Glide.with(this).load(imageUri).into(img);
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    }else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                }else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
        }
    }

    @TargetApi(19)
    // 4.4及以上系统
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];  // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" +id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型额uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的uri，则直接获取图片路径
            imagePath = uri.getPath();
        }
        filePath = imagePath;
        displayImage(imagePath);
    }

    // 4.4以下系统
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过uri和selection获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if(imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            img.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private void release() {

        commodityName = inputName.getText().toString();
        commodityDesc = inputDesc.getText().toString();
        String str = inputPrice.getText().toString();
        commodityPrice = Double.parseDouble(str);

        Log.d("newCommodity", "release: "+commodityName);
        Log.d("newCommodity", "release: "+commodityCategory);
        Log.d("newCommodity", "release: "+commodityPrice);
        Log.d("newCommodity", "release: "+commodityDesc);
        Log.d("newCommodity", "release: "+filePath);



        Commodity newCommodity = new Commodity();
        newCommodity.setName(commodityName);
        newCommodity.setCategory(commodityCategory);
        newCommodity.setPrice(commodityPrice);
        newCommodity.setOwner(owner);

        // 保存图片
        //newCommodity.setImagePath(commodityImage);

        newCommodity.setImagePath(filePath);
        newCommodity.setDescription(commodityDesc);
        newCommodity.save();
        setResult(RESULT_OK, intent);
        finish();
    }
}
