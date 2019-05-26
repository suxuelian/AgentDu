package com.example.root.express;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class OrderLab {
    private static OrderLab sOrderLab;
    private List<Order> mOrders;


    private void createOrder(){
        Order order = new Order();
        mOrders.add(order);

    }

    public Order getOrder(String id){
        for(Order order : mOrders){
            if(order.getObjectId().equals(id)){
                return order;
            }
        }
        return null;
    }

    //创建单例OrderLab
    public static OrderLab get(Context context){
        if (sOrderLab == null){
            sOrderLab = new OrderLab(context);
        }
        return sOrderLab;
    }

    private OrderLab(Context context){
        mOrders = new ArrayList<>();
        Log.i("dw", "OrderLab: +++++++++++++++++");
        /*bmob获取，并添加到list中*/
        BmobQuery<Order> query = new BmobQuery<Order>();
        query.order("-createAt");
        query.findObjects(new FindListener<Order>() {
                              @Override
                              public void done(List<Order> list, BmobException e) {
                                  if(e == null){
                                      //添加数据到orderList中
                                      mOrders.addAll(list);
                                      Log.i("order获取通知", "============================");
                                      Log.i("order获取通知order获取通知", mOrders.toString());

                                  }
                                  else {
                                      Log.i("order获取通知", "done: 获rderList取失败");
                                  }
                              }
                          });
    }

    //添加新的订单
    public void addOrder(Order o){
        mOrders.add(o);
    }


    public List<Order> getOrders() {
        return mOrders;
    }

    /*bmob获取，并添加到list中*/
//    private void connect() {
//        BmobQuery<Order> query = new BmobQuery<Order>();
//        query.order("-createAt");
//        query.findObjects(new FindListener<Order>() {
//            @Override
//            public void done(List<Order> list, BmobException e) {
//                if(e == null){
//                    //添加数据到orderList中
//                    mOrders.addAll(list);
//                    Log.i("order获取通知", list.toString());
//                }
//                else {
//                    Log.i("order获取通知", "done: 获rderList取失败");
//                }
//            }
//        });
//    }

}
