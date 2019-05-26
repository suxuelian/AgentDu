package com.example.root.express.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.express.Order;
import com.example.root.express.OrderLab;
import com.example.root.express.R;
import com.example.root.express.activity.OrderPagerActivity;
import com.example.root.express.activity.PromulgationActivity;
import com.example.root.express.activity.SelfActivity;

import java.util.List;

public class OrderListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mOrderRecyclerView;
    private OrderAdapter mAdapter;
    private boolean mSubtitleVisible;

    //User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        mOrderRecyclerView = (RecyclerView)view.findViewById(R.id.order_recycler_view);
        mOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
//        mUser = new User();
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("INFOR", Context.MODE_PRIVATE);
//        mUser.setUsername(sharedPreferences.getString("USERNAME",""));
//        mUser.setTel(sharedPreferences.getString("TELEPHONE",""));
//        mUser.setObjectId(sharedPreferences.getString("OBJECTID",""));

        updateUI();
        return view;
    }

    //在onResume()方法中刷新列表项
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_order_list, menu);

        //更新菜单项1
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    //标题栏
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            //添加新订单
            case R.id.new_order:
                Intent intent = new Intent(getActivity(), PromulgationActivity.class);
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                //响应show_subtitle菜单项点击事件
                //更新菜单项2
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            case R.id.mine:
                Intent intent1 = new Intent(getActivity(), SelfActivity.class);
                startActivity(intent1);
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    //设置工具栏子标题
    private void updateSubtitle(){
        OrderLab orderLab = OrderLab.get(getActivity());
        int orderCount = orderLab.getOrders().size();
        String subtitle = getString(R.string.subtitle_format, orderCount);

        //实现菜单项和子标题的联动
        if(mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    public void updateUI() {
        OrderLab orderLab = OrderLab.get(getActivity());
        List<Order> orders = orderLab.getOrders();

        if (mAdapter == null) {
            mAdapter = new OrderAdapter(orders);
            mOrderRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }


        updateSubtitle();

    }

    //在CrimeHolder中处理点击事件
    private class  OrderHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        //holder中的组件
        private TextView mAddressBeforeTextView;
        private TextView mAddressAfterTextView;
        private ImageView mSolvedImageView;
        private Order mOrder;

        //构造方法
        public OrderHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_order,parent,false));
            itemView.setOnClickListener(this);

            //holder中的组件
            mAddressBeforeTextView = (TextView)itemView.findViewById(R.id.address_bbefore);
            mAddressAfterTextView = (TextView)itemView.findViewById(R.id.address_aafter);
            mSolvedImageView = (ImageView)itemView.findViewById(R.id.order_ssolved);
        }

        //绑定列表项
        public void bind(Order order){
            mOrder = order;
            mAddressBeforeTextView.setText(order.getAddressBefore());
            mAddressAfterTextView.setText(order.getAddressAfter());
            mSolvedImageView.setVisibility(order.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
            //显示当前Order的详细信息，启动OrderActivity
            //Intent intent = new Intent(getActivity(), OrderActivity.class);
            //调用新的newIntent方法
            //Intent intent = OrderActivity.newIntent(getActivity(), mOrder.getObjectId());
            //配置启动OrderPagerActivity
            Intent intent = OrderPagerActivity.newIntent(getActivity(), mOrder.getObjectId());
            Log.i( mOrder.getObjectId(), "onClick:mOrder.getObjectId()!!!!!!!currentq!!!!!!!!!! ");
            startActivity(intent);
        }
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderHolder> {
        private List<Order> mOrders;

        public OrderAdapter(List<Order> orders) {
            mOrders = orders;
        }

        @Override
        public OrderHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new OrderHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(OrderHolder holder, int position) {
            Order order = mOrders.get(position);
            //调用绑定bind()方法
            holder.bind(order);
        }

        @Override
        public int getItemCount() {
            return mOrders.size();
        }

        public void setOrders(List<Order> orders){
            mOrders = orders;
        }
    }


}
