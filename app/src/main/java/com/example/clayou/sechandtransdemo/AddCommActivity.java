package com.example.clayou.sechandtransdemo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddCommActivity extends AppCompatActivity {

    private Spinner selCategory;
    private ImageView img;

    private List<String> categoryList;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comm);

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
        //bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.show();

        Button btn_gallery = bottomDialog.getWindow().findViewById(R.id.btn_gallery);
        Button btn_photo = bottomDialog.getWindow().findViewById(R.id.btn_photo);
        Button btn_cancel = bottomDialog.getWindow().findViewById(R.id.btn_cancel);


        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InlinedApi")
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
            }
        });

        btn_photo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InlinedApi")
            @Override
            public void onClick(View view) {
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
}
