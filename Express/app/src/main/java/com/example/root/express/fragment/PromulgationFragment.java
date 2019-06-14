package com.example.root.express.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.express.Order;
import com.example.root.express.R;
import com.example.root.express.activity.OrderListActivity;
import com.example.root.express.EnrolAndLogin.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class PromulgationFragment extends Fragment {


    Order mOrder;
    private TextView tvLabel;
    private Button btPublish;

    private TextView mBeforeAddressEditText;
    private TextView mAfterAddressEditText;
    private TextView mTakeCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragemnt_promulgation, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        tvLabel = (TextView)view.findViewById(R.id.promulgation_label);
        mBeforeAddressEditText = (TextView) view.findViewById(R.id.address_before);
        mAfterAddressEditText = (TextView) view.findViewById(R.id.address_after);
        mTakeCode = (TextView) view.findViewById(R.id.code);

        //发布订单
        btPublish = (Button)view.findViewById(R.id.publish_order);
        btPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               boolean flag = addOrder();
                if(flag) {
                    try {
                        Thread.sleep(1000); //休眠3秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), OrderListActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast toast =Toast.makeText(getActivity(), "请填写以上全部信息" , Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,500);
                    toast.show();
                }
            }
        });

    }

    //添加新订单,发布
    private boolean addOrder() {
        if(TextUtils.isEmpty(mBeforeAddressEditText.getText())
        || TextUtils.isEmpty(mAfterAddressEditText.getText())
        || TextUtils.isEmpty(mTakeCode.getText())){
            return false;
        }
        final Order order = new Order();
        order.setPromulgator(getCurrentUser());
        order.setAddressBefore(mBeforeAddressEditText.getText().toString());

        order.setAddressAfter(mAfterAddressEditText.getText().toString());
        order.setTakeCode(mTakeCode.getText().toString());
        order.setSolved(false);

        order.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Toast toast =Toast.makeText(getActivity(), "发布订单成功，订单号为：" + objectId, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getActivity(), "发布失败：" + e.getMessage(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        });
        Log.i(order.getTakeCode(), "onOptionsItemSelected: 添加订单+quhuoma");
        return true;
    }

    //获得当前用户
    public User getCurrentUser() {
        User currentUser = new User();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("INFOR", Context.MODE_PRIVATE);
        currentUser.setUsername(sharedPreferences.getString("USERNAME", ""));
        currentUser.setTel(sharedPreferences.getString("TELEPHONE", ""));
        currentUser.setObjectId(sharedPreferences.getString("OBJECTID", ""));

        return currentUser;
    }
}
