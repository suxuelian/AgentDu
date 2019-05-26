package com.example.root.express.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.root.express.Order;
import com.example.root.express.fragment.OrderFragment;
import com.example.root.express.OrderLab;
import com.example.root.express.R;

import java.util.List;
import java.util.UUID;

public class OrderPagerActivity extends AppCompatActivity {
    private static final String EXTRA_ORDER_ID = "com.example.root.express.order_id";

    private ViewPager mViewPager;
    private List<Order> mOrders;

    public static Intent newIntent(Context packageContext, String orderId){
        Intent intent = new Intent(packageContext, OrderPagerActivity.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pager);

        String orderId = (String)getIntent()
                .getSerializableExtra(EXTRA_ORDER_ID);

        mViewPager = (ViewPager)findViewById(R.id.order_view_pager);

        //从CrimeLab中获取数据集
        mOrders = OrderLab.get(this).getOrders();
        //获取activity的FragmentManager实例
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                Order order = mOrders.get(i);
                //创建并返回一个有效配置的OrderFragment
                return OrderFragment.newInstance(order.getObjectId());
            }

            @Override
            public int getCount() {
                return mOrders.size();
            }
        });

        //循环检查crime的id，设置ViewPager当前要显示大的列表项为制定id位置的列表项
        for(int i = 0; i < mOrders.size(); i++){
            if(mOrders.get(i).getObjectId().equals(orderId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

}
