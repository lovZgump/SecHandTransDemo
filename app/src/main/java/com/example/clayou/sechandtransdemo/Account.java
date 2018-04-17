package com.example.clayou.sechandtransdemo;

import org.litepal.crud.DataSupport;

/**
 * Created by 10295 on 2018/4/11.
 */

public class Account extends DataSupport {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
