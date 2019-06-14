package com.example.root.express.EnrolAndLogin;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
    public String getTel() {
        return mTel;
    }

    public void setTel(String tel) {
        mTel = tel;
    }

    private String mTel;


}
