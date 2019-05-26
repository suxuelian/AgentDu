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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.express.Order;
import com.example.root.express.OrderLab;
import com.example.root.express.R;
import com.example.root.express.loginAndEnroll.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;


public class OrderFragment extends Fragment {
    private static final String ARG_ORDER_ID = "order_id";


    Order mOrder;
    User mPromulgator;
    User mReciver;

    private Button btRecive;

    private TextView mOrderId;
    private TextView mBeoreAddressTextView;
    private TextView mAfterAddressTextView;
    private TextView mTakeCode;

    //发布者
    private TextView tvPromulgatorName;
    private TextView tvPromulgatorTel;

    private CheckBox mCheckBox;

    //接收者
     private EditText etReciverName;
     private EditText etReciverTel;

    //编写newInstance(String)方法
    public static OrderFragment newInstance(String orderId){
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
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        String orderId = (String)getArguments().getSerializable(ARG_ORDER_ID);
        mOrder = OrderLab.get(getActivity()).getOrder(orderId);
        Log.i(mOrder.getObjectId(), "onCreate:________点击后----- ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        init(view);

        return view;
    }


    private void init(View view) {
        mOrderId = (TextView)view.findViewById(R.id.order_id);
        mBeoreAddressTextView = (TextView) view.findViewById(R.id.address_before);
        mAfterAddressTextView = (TextView) view.findViewById(R.id.address_after);
        mTakeCode = (TextView) view.findViewById(R.id.code);

        mCheckBox = (CheckBox)view.findViewById(R.id.is_order_solved);
        mCheckBox.setClickable(false);
        mCheckBox.setChecked(mOrder.isSolved());

        //关联组件——发布者
        tvPromulgatorName = (TextView) view.findViewById(R.id.promulgator_name);
        tvPromulgatorTel = (TextView) view.findViewById(R.id.promulgator_tel);

        //关联组件-接收者
        mReciver = new User();
        mReciver = getCurrentUser();
        etReciverName = (EditText)view.findViewById(R.id.et_reciver_name);
        etReciverName.setHint(mReciver.getUsername());
        etReciverTel = (EditText)view.findViewById(R.id.et_reciver_tel);
        etReciverTel.setHint(mReciver.getTel());


        //从当前order获取数据
        //订单信息
        mOrderId.setText(mOrder.getObjectId());
        mBeoreAddressTextView.setText(mOrder.getAddressBefore());
        mAfterAddressTextView.setText(mOrder.getAddressAfter());
        mTakeCode.setText(mOrder.getTakeCode());

        //发布人姓名和电话
        getPromulgatorInfo(mOrder.getPromulgator().getObjectId());

        /*接收订单*/
        btRecive = (Button) view.findViewById(R.id.take_order);
        btRecive.setClickable(!mOrder.isSolved());
        btRecive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeOrder();
            }
        });
    }

    private void getPromulgatorInfo(String objectId) {
        //查找User表中id为objectId的数据
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    mPromulgator = user;
                    tvPromulgatorName.setText(user.getUsername());
                    tvPromulgatorTel.setText(user.getTel());
                    Log.i("查询订单的发布者xingm", user.getUsername());
                    Log.i("查询订单的发布者tel", user.getTel());

                }else {
                    Log.i("查询订单的发布者", "done: 查询失败");
                }
            }
        });
    }


    //接收订单
    private void takeOrder() {
        mOrder.setReceiver(mReciver);
        mOrder.update(mOrder.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                    Log.i("接收订单，更新订单数据", "done: 更新成功");
                }else {
                    Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_SHORT).show();
                    Log.i(e.toString(), "done: 更新失败");

                }
            }
        });
    }



    public User getCurrentUser() {
        User currentUser = new User();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("INFOR", Context.MODE_PRIVATE);
        currentUser.setUsername(sharedPreferences.getString("USERNAME", ""));
        currentUser.setTel(sharedPreferences.getString("TELEPHONE", ""));
        currentUser.setObjectId(sharedPreferences.getString("OBJECTID", ""));

        return currentUser;
    }
}
