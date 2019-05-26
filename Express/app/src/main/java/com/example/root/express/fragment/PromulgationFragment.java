package com.example.root.express.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.express.Order;
import com.example.root.express.R;
import com.example.root.express.loginAndEnroll.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class PromulgationFragment extends Fragment {


    Order mOrder;
    private Button btPublish;

    private TextView mBeoreAddressTextView;
    private TextView mAfterAddressTextView;
    private TextView mTakeCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragemnt_promulgation, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        mBeoreAddressTextView = (TextView) view.findViewById(R.id.address_before);
        mAfterAddressTextView = (TextView) view.findViewById(R.id.address_after);
        mTakeCode = (TextView) view.findViewById(R.id.code);

        //发布订单
        btPublish = (Button)view.findViewById(R.id.publish_order);
        btPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrder();
            }
        });

    }

    //添加新订单,发布
    private String addOrder() {
        final Order order = new Order();
        order.setPromulgator(getCurrentUser());
        order.setAddressBefore(mBeoreAddressTextView.getText().toString());
        order.setAddressAfter(mAfterAddressTextView.getText().toString());
        order.setTakeCode(mTakeCode.getText().toString());
        order.setSolved(false);

        order.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Toast.makeText(getActivity(), "添加数据成功，返回objectId为：" + objectId, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.i(order.getTakeCode(), "onOptionsItemSelected: 添加订单+quhuoma");
        return order.getObjectId();
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
