package com.example.root.express.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.express.Order;
import com.example.root.express.OrderLab;
import com.example.root.express.R;
import com.example.root.express.activity.OrderListActivity;
import com.example.root.express.EnrolAndLogin.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class EditOrderFragment extends Fragment {
    private static final String ARG_ORDER_ID = "order_id";

    Order mOrder;
    private TextView tvLabel;
    private Button btSave;
    private Button btDelete;

    private TextView mBeforeAddressEditText;
    private TextView mAfterAddressEditText;
    private TextView mTakeCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_order, container, false);

        init(view);

        return view;
    }

    //编写newInstance(String)方法
    public static EditOrderFragment newInstance(String orderId) {
        //创建fragment argument
        //首先创建Bundle对象
        Bundle args = new Bundle();
        //然后使用Bundle限定类型的put方法，将argument添加到bundle中
        args.putSerializable(ARG_ORDER_ID, orderId);

        EditOrderFragment fragment = new EditOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private void init(View view) {
        String orderId = (String) getArguments().getSerializable(ARG_ORDER_ID);
        mOrder = OrderLab.get(getActivity()).getOrder(orderId);

        tvLabel = (TextView) view.findViewById(R.id.promulgation_label);
        mBeforeAddressEditText = (TextView) view.findViewById(R.id.address_before);
        mAfterAddressEditText = (TextView) view.findViewById(R.id.address_after);
        mTakeCode = (TextView) view.findViewById(R.id.code);

        //获取修改订单之前数据
        mBeforeAddressEditText.setText(mOrder.getAddressBefore());
        mAfterAddressEditText.setText(mOrder.getAddressAfter());
        mTakeCode.setText(mOrder.getTakeCode());

        //修改订单
        btSave = (Button) view.findViewById(R.id.save_order);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = saveOrder();
                if (flag) {
                    try {
                        Thread.sleep(1000); //休眠3秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), OrderListActivity.class);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getActivity(), "请填写以上全部信息", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 500);
                    toast.show();
                }
            }
        });

        //删除订单
        btDelete = (view).findViewById(R.id.delete_order);
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = deleteOrder();
                if (flag) {
                    try {
                        Thread.sleep(1000); //休眠3秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    //修改订单，保存
    private boolean saveOrder() {
        if (TextUtils.isEmpty(mBeforeAddressEditText.getText())
                || TextUtils.isEmpty(mAfterAddressEditText.getText())
                || TextUtils.isEmpty(mTakeCode.getText())) {
            return false;
        }
        mOrder.setAddressBefore(mBeforeAddressEditText.getText().toString());

        mOrder.setAddressAfter(mAfterAddressEditText.getText().toString());
        mOrder.setTakeCode(mTakeCode.getText().toString());
        mOrder.setSolved(false);

        mOrder.update(mOrder.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast toast = Toast.makeText(getActivity(), "修改订单成功", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getActivity(), "修改失败：" + e.getMessage(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        return true;
    }


    private boolean deleteOrder() {
        final boolean[] flag = {false};
        mOrder.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast toast = Toast.makeText(getActivity(), "已删除订单号为" + mOrder.getObjectId() +"的订单", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), OrderListActivity.class);
                    startActivity(intent);
                    flag[0] = true;
                } else {
                    Toast toast = Toast.makeText(getActivity(), "删除失败：" + e.getMessage(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        return flag[0];
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
