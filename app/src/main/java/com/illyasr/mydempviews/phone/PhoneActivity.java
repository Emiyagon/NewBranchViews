package com.illyasr.mydempviews.phone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.illyasr.mydempviews.R;
import com.illyasr.mydempviews.databinding.ActivityPhoneBinding;
import com.illyasr.mydempviews.util.StatusBarUtil;
import com.illyasr.mydempviews.util.StatusUtils;

import pub.devrel.easypermissions.EasyPermissions;

public class PhoneActivity extends AppCompatActivity {

    private ActivityPhoneBinding binding;
    private  String[] messions = new String[]{
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_phone);
        //  沉浸式状态栏
        StatusUtils.transparentStatusBar(this, true);
        StatusBarUtil.setDarkMode(this, true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone);



        OnDate();
    }

    private void OnDate() {
        binding.tvTxl.setOnClickListener(v -> {
//            new PhoneUtil(this).getPhone();
            if (EasyPermissions.hasPermissions(this, messions)) {
                new PhoneDialog(this,  new PhoneUtil(this).getPhone())
                        .setClick((name, tele) -> {
                            binding.tvTxl.setText(name+"--"+tele);
                        })
                        .show();
            } else {
                EasyPermissions.requestPermissions(this, getResources().getString(R.string.toast_1), 100, messions);
            }

        });

    }
}