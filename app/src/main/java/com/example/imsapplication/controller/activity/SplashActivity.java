package com.example.imsapplication.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
;
import com.example.imsapplication.R;
import com.example.imsapplication.modle.Model;
import com.example.imsapplication.modle.bean.UserInfo;
import com.hyphenate.chat.EMClient;

/**
 * Created by 吕文杰 on 2018/4/10.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slapsh);

        //判断进入主页面还是进入我们的登录页面
        toMainOrLogin();

        
    }
    //判断进入主页面还是进入我们的登录页面
    private void toMainOrLogin() {



        //判断当前页面是否已经登录
        if(EMClient.getInstance().isLoggedInBefore()){
            //获取我们用户登录信息

            UserInfo account= Model.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());
            Model.getInstance().loginSuccess(account);
            toMain();
        }else{
            toLogin();
        }

    }

    private void toLogin() {

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                }
                Intent in=new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(in);
                finish();
            }
        });

    }

    //跳转到主页面的方法
    private void toMain() {

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent in = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }

        });
    }


}


