package com.example.root.express.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.root.express.fragment.EditOrderFragment;

public class EditOrderActivity extends SingleFragmentActivity {
    private static final String EXTRA_ORDER_ID = "com.example.root.express.order_id";

    @Override
    protected Fragment createFragment() {
        String orderId = (String)getIntent()
                .getSerializableExtra(EXTRA_ORDER_ID);
        return EditOrderFragment.newInstance(orderId);
    }

    public static Intent newIntent(Context packageContext, String orderId){
        Intent intent = new Intent(packageContext, EditOrderActivity.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        return intent;
    }
}
