package com.example.imsapplication.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.imsapplication.R;
import com.example.imsapplication.modle.Model;
import com.example.imsapplication.modle.bean.UserInfo;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by 吕文杰 on 2018/4/11.
 */
//登录页面
public class LoginActivity extends Activity {
    private EditText ed_login_name,ed_login_pwd;
    private Button bt_login_regist,bt_login_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化控件
        initView();
        //初始化监听事件
        initListener();
    }
    //初始化监听事件
    private void initListener() {
        //注册的监听事件
        bt_login_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regist();
            }
        });
        //登录的监听事件
        bt_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            } 
        });
    }
    //登录按钮的页面逻辑
    private void login() {
        //1 获取输入的用户名和密码
        final String loginName=ed_login_name.getText().toString();
        final String loginPwd=ed_login_pwd.getText().toString();
        //2 校验输入的用户名和密码
        if (TextUtils.isEmpty(loginName)||TextUtils.isEmpty(loginPwd)){
            Toast.makeText(LoginActivity.this,"输入的用户名和密码不能为空",Toast.LENGTH_LONG).show();
            return;
        }
        //3 登录逻辑的处理
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //去环信服务器登录
                EMClient.getInstance().login(loginName, loginPwd, new EMCallBack() {
                    //登录成功
                    @Override
                    public void onSuccess() {
                            //对模型层的数据处理
                            Model.getInstance().loginSuccess(new UserInfo(loginName));
                        //保存用户信息到本地数据库
                        Model.getInstance().getUserAccountDao().addAccount(new UserInfo(loginName));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //提示登录成功
                                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                                //提示跳转页面
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    //登录失败
                    @Override
                    public void onError(int i, String s) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //提示登录失败
                                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                    //登录过程处理
                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });





    }

    //注册的业务逻辑处理
    private void regist() {
        //1 获取输入的用户名和密码
        final String registName=ed_login_name.getText().toString();
        final String registPwd=ed_login_pwd.getText().toString();
        //2 校验输入的用户名和密码
        if (TextUtils.isEmpty(registName)||TextUtils.isEmpty(registPwd)){
            Toast.makeText(LoginActivity.this,"输入的用户名和密码不能为空",Toast.LENGTH_LONG).show();
            return;
        }
        //3 去服务器注册账号 (服务器是环信的服务器)
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //去环信服务器注册账号
                    EMClient.getInstance().createAccount(registName,registPwd);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"注册失败:"+e.toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    //初始化控件
    private void initView() {
        ed_login_name=(EditText) findViewById(R.id.ed_login_name);
        ed_login_pwd=(EditText) findViewById(R.id.ed_login_pwd);
        bt_login_regist=(Button) findViewById(R.id.bt_login_regist);
        bt_login_login=(Button) findViewById(R.id.bt_login_login);
    }
}
