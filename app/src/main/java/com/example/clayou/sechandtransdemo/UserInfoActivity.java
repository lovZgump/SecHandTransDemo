package com.example.clayou.sechandtransdemo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // 实现按两次返回键退出应用
    private static boolean isExit;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private String username;
    private ImageView avatar;

    private List<Commodity> commodityList = new ArrayList<>();
    private CommodityRecyAdapter commodityRecyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoActivity.this, AddCommActivity.class);
                intent.putExtra("owner", username);
                startActivityForResult(intent, 1);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new DataInit().execute();
//        adapter = new CommodityAdapter(UserInfoActivity.this, R.layout.commodity, commodityList);
//        ListView listView = findViewById(R.id.list_view);
//        listView.setAdapter(adapter);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("bmob", "done: i'm here");
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(UserInfoActivity.this, 2);
        recyclerView.setLayoutManager(layoutManager);
        Log.d("bmob", "onCreate: "+commodityList.size());
        commodityRecyAdapter = new CommodityRecyAdapter(commodityList);
        recyclerView.setAdapter(commodityRecyAdapter);

        NavigationView navView = findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        avatar = headerView.findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseAvatar();
            }
        });
    }

    // 换头像
    private void chooseAvatar() {

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
                //selectPicInGallery();
                bottomDialog.dismiss();
            }
        });

        btn_photo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InlinedApi")
            @Override
            public void onClick(View view) {
                //takePhoto();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    initCommodities();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    refreshList();
                }
                break;
            default:
        }
    }

    // 发布新商品后，刷新列表
    private void refreshList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        commodityRecyAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            commodityRecyAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            Toast.makeText(UserInfoActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void initCommodities(){

        commodityList.clear();
//        Cursor cursor = DataSupport.findBySQL("select * from Commodity where owner = ?", username);
//        if(cursor.moveToFirst()){
//            do {
//                Commodity commodity = new Commodity();
//                commodity.setName(cursor.getString(cursor.getColumnIndex("name")));
//                commodity.setCategory(cursor.getString(cursor.getColumnIndex("category")));
//                commodity.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
//                commodity.setImagePath(cursor.getString(cursor.getColumnIndex("imagepath")));
//                commodity.setDescription(cursor.getString(cursor.getColumnIndex("description")));
//                commodity.setOwner(username);
//                commodityList.add(commodity);
//            }while (cursor.moveToNext());
//        }
//        cursor.close();

//        Log.d("flag", "initCommodities:" + username );
//        List<Commodity> commodities = DataSupport.where("owner = ?", username).find(Commodity.class);
//        Log.d("flag", "initCommodities: i'm here"+commodities.size() );
//        for(Commodity commodity : commodities){
//            Log.d("flag", "initCommodities: " + commodity.getName());
//            commodityList.add(commodity);
//        }

        BmobQuery<Commodity> query = new BmobQuery<>();
        query.setLimit(50);
        query.findObjects(new FindListener<Commodity>() {
            @Override
            public void done(List<Commodity> list, BmobException e) {
                Log.d("bmob", "done: "+list.size());
                if(e == null) {
                    for (Commodity commodity : list) {
                        Log.d("bmob", "done: "+commodity.getName()+commodity.getOwner()+"  "+commodity.getImagePath());
                        commodityList.add(commodity);
                    }
                }else {
                    Toast.makeText(UserInfoActivity.this, "失败", Toast.LENGTH_SHORT).show();
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
                Log.d("bmob", "done: i'm here");
            }
        });
        Log.d("bmob", "done: i'm here");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return  false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次返回键退出应用", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        }
    }

    public class DataInit extends AsyncTask<Void, Void, Boolean> {
        DataInit(){}

        @Override
        protected Boolean doInBackground(Void... voids) {
            initCommodities();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                //finish();
                Log.d("bmob", "onPostExecute: "+commodityList.size());
                commodityRecyAdapter.notifyDataSetChanged();
            }
        }
    }

}
