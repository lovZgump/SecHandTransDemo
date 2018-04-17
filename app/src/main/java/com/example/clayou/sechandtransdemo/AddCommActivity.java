package com.example.clayou.sechandtransdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddCommActivity extends AppCompatActivity {

    private Spinner selCategory;

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
    }
}
