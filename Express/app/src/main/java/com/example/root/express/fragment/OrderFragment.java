package com.example.root.express.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.express.Order;
import com.example.root.express.OrderLab;
import com.example.root.express.R;
import com.example.root.express.activity.EditOrderActivity;
import com.example.root.express.activity.OrderListActivity;
import com.example.root.express.EnrolAndLogin.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;


public class OrderFragment extends Fragment {
    private static final String ARG_ORDER_ID = "order_id";
    private static boolean SOLVED_FLAG = false;


    Order mOrder;
    User mPromulgator;
    User mReciver;

    //取货码
    TextView takeCode;

    private Button btRecive;
    private Button btEdit;

    //发布者
    private TextView tvPromulgatorName;
    private TextView tvPromulgatorTel;

    //接收者
    private TextView etReciverName;
    private TextView etReciverTel;
    private ConstraintLayout clReciverLayout;

    //编写newInstance(String)方法
    public static OrderFragment newInstance(String orderId) {
        //创建fragment argument
        //首先创建Bundle对象
        Bundle args = new Bundle();
        //然后使用Bundle限定类型的put方法，将argument添加到bundle中
        args.putSerializable(ARG_ORDER_ID, orderId);

        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String orderId = (String) getArguments().getSerializable(ARG_ORDER_ID);
        mOrder = OrderLab.get(getActivity()).getOrder(orderId);
        Log.i(mOrder.getObjectId(), "onCreate:________点击后----- ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        clReciverLayout = (ConstraintLayout) view.findViewById(R.id.revicer_layout);

        init(view);

        return view;
    }


    private void init(View view) {
        TextView orderId = (TextView) view.findViewById(R.id.order_id);
        TextView beoreAddressTextView = (TextView) view.findViewById(R.id.address_before);
        TextView afterAddressTextView = (TextView) view.findViewById(R.id.address_after);
        btRecive = (Button) view.findViewById(R.id.take_order);
        btEdit = (Button) view.findViewById(R.id.edit_order);
        takeCode = (TextView) view.findViewById(R.id.code);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.is_order_solved);
        checkBox.setClickable(false);
        checkBox.setChecked(mOrder.isSolved());

        //关联组件——发布者
        tvPromulgatorName = (TextView) view.findViewById(R.id.promulgator_name);
        tvPromulgatorTel = (TextView) view.findViewById(R.id.promulgator_tel);

        //关联组件-接收者
        mReciver = new User();
        mReciver = getCurrentUser();
        etReciverName = (TextView) view.findViewById(R.id.et_reciver_name);
        etReciverName.setText(mReciver.getUsername());
        etReciverTel = (TextView) view.findViewById(R.id.et_reciver_tel);
        etReciverTel.setText(mReciver.getTel().substring(0, 3) + "****" + mReciver.getTel().substring(7));


        //从当前order获取数据
        //订单信息
        orderId.setText(mOrder.getObjectId());
        beoreAddressTextView.setText(mOrder.getAddressBefore());
        afterAddressTextView.setText(mOrder.getAddressAfter());

        //根据情况设置取货码的可见性
        setTakeCodeSate();

        //发布人姓名和电话
        promulgatorInfo(mOrder.getPromulgator().getObjectId());

        /*接收订单*/
        setReciveButtonVisibility(mOrder.isSolved());
        btRecive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeOrder();
                if (SOLVED_FLAG) {
                    mOrder.setSolved(true);
                    //接单成功返回订单池页面
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), OrderListActivity.class);
                    startActivity(intent);

                }
            }
        });

        /*编辑订单*/
        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 editOrder();
            }
        });


    }

    private boolean editOrder() {
        /*首先检查订单是否为本人发布的订单
         * 如果是，则该订单能被当前用户接收，启动修改界面
         * 如果不是，则不可以接收，直接返回false
         */
        String promulgatorId = mPromulgator.getObjectId();
        if (promulgatorId.equals(mReciver.getObjectId())) {
            Intent intent = EditOrderActivity.newIntent(getActivity(), mOrder.getObjectId());
            startActivity(intent);
            getActivity().finish();
            return true;
        }else{
            Toast toast = Toast.makeText(getActivity(), "不能修改别人发布的订单", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }
    }


    /*设置取货码的可见性*/
    private void setTakeCodeSate() {
        if (mOrder.isSolved()) {
            if (mOrder.getReceiver().getObjectId().equals(getCurrentUser().getObjectId())) {
                takeCode.setText(mOrder.getTakeCode());
            } else {
                takeCode.setText("******");
            }
        } else {
            if (mOrder.getPromulgator().getObjectId().equals(getCurrentUser().getObjectId())) {
                takeCode.setText(mOrder.getTakeCode());
            } else {
                takeCode.setText("******");
            }
        }
    }

    //根据objectId得到并设置发布者信息
    private void promulgatorInfo(String objectId) {
        //查找User表中id为objectId的数据
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    mPromulgator = user;
                    tvPromulgatorName.setText(user.getUsername());
                    //模糊手机号，保护用户隐私
                    tvPromulgatorTel.setText(user.getTel().substring(0, 3) + "****" + user.getTel().substring(7));
                    Log.i("查询订单的发布者", "done: 查询成功");

                } else {
                    Log.i("查询订单的发布者", "done: 查询失败");
                }
            }
        });
    }


    //接收订单
    private boolean takeOrder() {
        /*首先检查订单是否为本人发布的订单
         * 如果是，则该订单不能被当前用户接收，直接返回false
         * 如果不是，则可以接收
         */
        String promulgatorId = mPromulgator.getObjectId();
        if (promulgatorId.equals(mReciver.getObjectId())) {
            Toast toast = Toast.makeText(getActivity(), "不能接收自己发布的订单", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }
        mOrder.setReceiver(mReciver);
        mOrder.setSolved(true);
        mOrder.update(mOrder.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    SOLVED_FLAG = true;
                    Toast toast = Toast.makeText(getActivity(), "接单成功", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    Log.i("接收订单，更新订单数据", "done: 更新成功");
                } else {
                    Toast toast = Toast.makeText(getActivity(), "接单失败", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Log.i(e.toString(), "done: 更新失败");

                }
            }

        });
        return SOLVED_FLAG;
    }


    public User getCurrentUser() {
        User currentUser = new User();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("INFOR", Context.MODE_PRIVATE);
        currentUser.setUsername(sharedPreferences.getString("USERNAME", ""));
        currentUser.setTel(sharedPreferences.getString("TELEPHONE", ""));
        currentUser.setObjectId(sharedPreferences.getString("OBJECTID", ""));

        return currentUser;
    }

    public void setReciveButtonVisibility(boolean visibility) {
        if (visibility) {
            clReciverLayout.setVisibility(View.VISIBLE);
            btRecive.setVisibility(View.INVISIBLE);
        } else {
            clReciverLayout.setVisibility(View.INVISIBLE);
            btRecive.setVisibility(View.VISIBLE);
        }
    }
}
