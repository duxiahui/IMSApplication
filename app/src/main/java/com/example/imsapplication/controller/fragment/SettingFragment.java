package com.example.imsapplication.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.example.imsapplication.R;
import com.example.imsapplication.controller.activity.LoginActivity;
import com.example.imsapplication.modle.Model;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by 吕文杰 on 2018/4/12.
 */

public class SettingFragment extends Fragment {
    private Button bt_setting_out;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=View.inflate(getActivity(), R.layout.activity_setting,null);
            initView(v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        //在Button上显示当前用户
        bt_setting_out.setText("退出登录("+ EMClient.getInstance().getCurrentUser()+")");
        //退出的逻辑
        bt_setting_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        //退出登录的环信服务器
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            //退出成功
                            @Override
                            public void onSuccess() {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //提示退出成功
                                            Toast.makeText(getActivity(),"退出成功",Toast.LENGTH_SHORT).show();
                                            //回到登录页面
                                            Intent in=new Intent(getActivity(), LoginActivity.class);
                                            startActivity(in);
                                            getActivity().finish();
                                        }
                                    });
                            }
                            //退出失败
                            @Override
                            public void onError(int i, String s) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"退出失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            //退出过程中
                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                    }
                });

            }
        });
    }

    private void initView(View v) {
        bt_setting_out=(Button)v.findViewById(R.id.bt_setting_out);
    }
}
