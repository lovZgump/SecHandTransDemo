package com.example.clayou.sechandtransdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;

    private EditText accountEdit;
    private EditText passwordEdit;

    private String username;
    private String password;

    private boolean isExit;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DataSupport.deleteAll(Commodity.class, "owner = ?", "Admin");
        this.initApp();



        pref = PreferenceManager.getDefaultSharedPreferences(this);

        accountEdit = findViewById(R.id.input_account);
        passwordEdit = findViewById(R.id.input_password);
        rememberPass = findViewById(R.id.remember_pass);

        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        boolean isRemember = pref.getBoolean("remember_password", false);
        if(isRemember){
            // 将账号和密码都设置到文本框中
            username = pref.getString("username", "");
            password = pref.getString("password", "");
            accountEdit.setText(username);
            accountEdit.setSelection(username.length());
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }


        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    String newUsername = data.getStringExtra("newUsername");
                    String newPassword = data.getStringExtra("newPassword");
                    accountEdit.setText(newUsername);
                    passwordEdit.setText(newPassword);
                    accountEdit.setSelection(newUsername.length());
                }
                break;
            default:
        }
    }

    public void initApp(){
        Connector.getDatabase();
    }

    private void attemptLogin(){
        username = accountEdit.getText().toString();
        password = passwordEdit.getText().toString();

//                Log.d("account", username);
//                Log.d("password", "123");
//                Log.d("password", password);

        // 判断用户名、密码
        List<Account> accounts =  DataSupport.where("username = ?", username).find(Account.class);


        if(accounts.isEmpty() || !(accounts.get(0).getPassword().equals(password))){
            Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }else if(accounts.get(0).getPassword().equals(password)) {
            editor = pref.edit();
            if (rememberPass.isChecked()) {
                editor.putBoolean("remember_password", true);
                editor.putString("username", username);
                editor.putString("password", password);
            } else {
                editor.putBoolean("remember_password", true);
                editor.putString("username", username);
                editor.putString("password", "");
            }
            editor.apply();

            Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
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

}
