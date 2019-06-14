package com.example.root.express.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.express.OrderLab;
import com.example.root.express.R;
import com.example.root.express.activity.OrderListActivity;
import com.example.root.express.activity.PromulgationActivity;
import com.example.root.express.EnrolAndLogin.LoginActivity;
import com.example.root.express.EnrolAndLogin.User;


public class SelfFragment extends Fragment {
    SharedPreferences sharedPreferences;

    User mUser;
    TextView tvName;
    TextView tvId;
    TextView tvTel;
    Button btLogout;
    Button btEdit;
    Button btAddOrder;
    Button btMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_self, container, false);
        tvName  = (TextView)view.findViewById(R.id.tv_name);

        sharedPreferences = getActivity().getSharedPreferences("INFOR", Context.MODE_PRIVATE);

        mUser = new User();
        mUser.setUsername(sharedPreferences.getString("USERNAME",""));
        mUser.setTel(sharedPreferences.getString("TELEPHONE",""));
        mUser.setObjectId(sharedPreferences.getString("OBJECTID",""));


        OrderLab orderLab = OrderLab.get(getActivity());
        //用户名
        tvName.setText(mUser.getUsername());

        //id
        tvId = (TextView)view.findViewById(R.id.tv_id);
        tvId.setText(mUser.getObjectId());

        //Tel
        tvTel = (TextView)view.findViewById(R.id.tv_tel);
        tvTel.setText(mUser.getTel());


        //退出登录
        btLogout = (Button)view.findViewById(R.id.logout_button);
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent  = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

                Toast toast =Toast.makeText(getActivity(), "退出登录成功", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,500);
                toast.show();

            }
        });

        //编辑
        btEdit = (Button)view.findViewById(R.id.edit_button);
        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击编辑按钮，启动编辑界面
//                Intent intent  = new Intent(getActivity(), EditActivity.class);
//                startActivity(intent);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.bottomLayout, new EditInfoFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //添加新订单
        btAddOrder = (Button)view.findViewById(R.id.add_button) ;
        btAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PromulgationActivity.class);
                startActivity(intent);
            }
        });


        // 主菜单
        btMain = (Button)view.findViewById(R.id.main_button);
        btMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrderListActivity.class);
                startActivity(intent);
            }
        });

        init();
        return view;
    }


    public void init(){

    }

}
