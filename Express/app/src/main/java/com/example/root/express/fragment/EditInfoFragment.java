package com.example.root.express.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.express.R;
import com.example.root.express.loginAndEnroll.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;


public class EditInfoFragment extends Fragment {
    EditText etName;
    EditText etPassword;
    EditText etTelphone;
    Button btSaved;
    SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_edit_info, container,false);

      sharedPreferences = getActivity().getSharedPreferences("INFOR", Context.MODE_PRIVATE);


        //用户、密码、电话
        etName = (EditText)view.findViewById(R.id.edit_name);
        etPassword =(EditText)view.findViewById(R.id.edit_password);
        etTelphone = (EditText)view.findViewById(R.id.telephone);

        final BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(sharedPreferences.getString("OBJECTID", ""), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    etName.setText(user.getUsername());
                    etPassword.setText(sharedPreferences.getString("USERPASSWORD", ""));
                    etTelphone.setText(user.getTel());
                }
            }
        });



        //保存按钮
        btSaved = (Button)view.findViewById(R.id.save_button);
        btSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });


        return view;
    }

    public void update(){
        String account_id = etName.getText().toString();
        String account_password = etPassword.getText().toString();
        String account_tel = etTelphone.getText().toString();

        //修改本机存储的数据
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USERNAME", account_id);
        editor.putString("USERPASSWORD", account_password);
        editor.putString("TELEPHONE",account_tel);
        editor.commit(); //提交修改


        //修改云数据
        User user = new User();
        user.setUsername(account_id);
        user.setPassword(account_password);
        user.setTel(account_tel);
        user.update(sharedPreferences.getString("OBJECTID", ""), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Toast.makeText(getActivity(),"保存成功", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
