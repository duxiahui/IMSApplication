package com.example.imsapplication.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imsapplication.R;
import com.example.imsapplication.modle.Model;
import com.example.imsapplication.modle.bean.UserInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by 吕文杰 on 2018/4/13.
 */
//添加联系人页面
public class AddContactActivity extends Activity {
    private EditText et_add_name;
    private Button bt_add_add;
    private TextView tv_add_find;
    private TextView tv_add_name;
    private RelativeLayout rl_add;
    private UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        //初始化
        initView();
        //监听事件
        initListener();
    }

    private void initListener() {
        //查找联系人的点击事件
        tv_add_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find();
            }
        });
        //添加按钮的点击事件
        bt_add_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               add();
            }
        });
    }
    //添加逻辑处理的点击事件
    private void add() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //去环信服务器添加好友
                try {
                    EMClient.getInstance().contactManager().addContact(userInfo.getName(),"添加好友");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this,"发送添加好友邀请成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this,"发送添加好友邀请失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    //查找按钮的处理事件
    private void find() {
        //获取用户的输入名称
       final String name=et_add_name.getText().toString();
        //校验获取的输入名称
        if (TextUtils.isEmpty(name)){
            Toast.makeText(AddContactActivity.this,"输入的用户名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        //去环信判断当前用户是否存在
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //去环信服务器判断当前查找的用户是否存在
                userInfo=new UserInfo(name);

                //更新UI显示
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_add.setVisibility(View.VISIBLE);
                        tv_add_name.setText(userInfo.getName());
                    }
                });

            }
        });



    }

    private void initView() {
        et_add_name=(EditText) findViewById(R.id.et_add_name);
        bt_add_add=(Button) findViewById(R.id.bt_add_add);
        tv_add_find=(TextView) findViewById(R.id.tv_add_find);
        tv_add_name=(TextView) findViewById(R.id.tv_add_name);
        rl_add=(RelativeLayout) findViewById(R.id.rl_add);


    }
}
