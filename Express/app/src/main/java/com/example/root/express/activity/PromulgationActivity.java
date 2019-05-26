package com.example.root.express.activity;

import android.support.v4.app.Fragment;

import com.example.root.express.fragment.PromulgationFragment;

public class PromulgationActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new PromulgationFragment();
    }
}
