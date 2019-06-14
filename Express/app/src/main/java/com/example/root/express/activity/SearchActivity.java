package com.example.root.express.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.express.Order;
import com.example.root.express.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class SearchActivity extends AppCompatActivity {
    private EditText etOrderId;
    private Button mSearchButton;
    private String mOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);
        init();
    }

    private void init() {
        etOrderId = (EditText)findViewById(R.id.et_order_id);
        mSearchButton = (Button)findViewById(R.id.search_order);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    private void search() {
        mOrderId = etOrderId.getText().toString();
        if(mOrderId.equals("")) {
            Toast toast = Toast.makeText(this, "查询的订单号不能为空", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }else {
            //在数据库中查找
            BmobQuery<Order> bmobQuery = new BmobQuery<>();
            bmobQuery.getObject(mOrderId, new QueryListener<Order>() {
                @Override
                public void done(Order order, BmobException e) {
                    if (e==null){
                    //查询成功
                    Toast toast = Toast.makeText(SearchActivity.this, "查询成功", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    finish();
                    Intent intent = OrderPagerActivity.newIntent(SearchActivity.this, mOrderId);
                    startActivity(intent);
                    }else {
                        Toast toast = Toast.makeText(SearchActivity.this, "没有订单号为"+mOrderId+"的订单", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            });
        }
    }
}
