package com.example.root.express;

import com.example.root.express.EnrolAndLogin.User;

import cn.bmob.v3.BmobObject;

public class Order extends BmobObject {

    private User mPromulgator; //发布者
    private User mReceiver; //接收者
    private String mAddressBefore; //取货点
    private String mAddressAfter;   //送货点
    private String mTakeCode;   //取货码
    private boolean mSolved;    //订单是否被接收

    public String getTakeCode() {
        return mTakeCode;
    }

    public void setTakeCode(String takeCode) {
        mTakeCode = takeCode;
    }

    public Order() {
    }


    public User getPromulgator() {
        return mPromulgator;
    }

    public void setPromulgator(User promulgator) {
        mPromulgator = promulgator;
    }

    public User getReceiver() {
        return mReceiver;
    }

    public void setReceiver(User mReceiver) {
        this.mReceiver = mReceiver;
    }

    public String getAddressBefore() {
        return mAddressBefore;
    }

    public void setAddressBefore(String addressBefore) {
        mAddressBefore = addressBefore;
    }

    public String getAddressAfter() {
        return mAddressAfter;
    }

    public void setAddressAfter(String addressAfter) {
        mAddressAfter = addressAfter;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }


}
