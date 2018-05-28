package com.example.clayou.sechandtransdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;

    private EditText accountEdit;
    private EditText passwordEdit;

    private String username;
    private String password;

    private View mProgressView;
    //private View mLoginFormView;

    private UserLoginTask mAuthTask = null;

    private int flag = 1;

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


        Bmob.initialize(this, "ea5318efd3001a38058c1bb32a2dc57d");

        //DataSupport.deleteAll(Commodity.class, "owner = ?", "Admin");
        this.initApp();



        pref = PreferenceManager.getDefaultSharedPreferences(this);

        accountEdit = findViewById(R.id.input_account);
        passwordEdit = findViewById(R.id.input_password);
        rememberPass = findViewById(R.id.remember_pass);
        mProgressView = findViewById(R.id.login_progress);


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

        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        accountEdit.setError(null);
        passwordEdit.setError(null);

        username = accountEdit.getText().toString();
        password = passwordEdit.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
           // showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }

        // 判断用户名、密码
//        List<Account> accounts =  DataSupport.where("username = ?", username).find(Account.class);
//
//
//        if(accounts.isEmpty() || !(accounts.get(0).getPassword().equals(password))){
//            Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
//        }else if(accounts.get(0).getPassword().equals(password)) {
//            editor = pref.edit();
//            if (rememberPass.isChecked()) {
//                editor.putBoolean("remember_password", true);
//                editor.putString("username", username);
//                editor.putString("password", password);
//            } else {
//                editor.putBoolean("remember_password", true);
//                editor.putString("username", username);
//                editor.putString("password", "");
//            }
//            editor.apply();
//
//            Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
//            intent.putExtra("username", username);
//            startActivity(intent);
//        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            //mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... Params) {
            // TODO: attempt authentication against a network service.
            BmobQuery<Account> query = new BmobQuery<Account>();
            query.addWhereEqualTo("username", username);
            query.findObjects(new FindListener<Account>() {
                @Override
                public void done(List<Account> list, BmobException e) {
                    if(list.isEmpty() || !(list.get(0).getPassword().equals(password))){
                        //Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        flag = 0;
                    }else if(list.get(0).getPassword().equals(password)) {
                        flag = 1;
                    }
                }
            });

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return flag;
        }

        @Override
        protected void onPostExecute(final Integer flag) {
            mAuthTask = null;
            showProgress(false);

            Log.d("bool", "onPostExecute: "+flag);

            if (flag == 1) {
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

            } else {
                Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                passwordEdit.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
