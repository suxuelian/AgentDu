package com.example.root.express.loginAndEnroll;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class User extends BmobUser {
    public String getTel() {
        return mTel;
    }

    public void setTel(String tel) {
        mTel = tel;
    }

    private String mTel;


}
