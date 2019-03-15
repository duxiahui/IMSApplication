package com.example.imsapplication.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.example.imsapplication.R;
import com.example.imsapplication.modle.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by 吕文杰 on 2018/4/25.
 */
//
public class NewGroupActivity  extends Activity{
    private EditText et_newgroup_name;
    private EditText et_newgroup_desc;
    private CheckBox cb_newgroup_public;
    private CheckBox cb_newgroup_invite;
    private Button bt_newgroup_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        initView();
        initListener();

    }

    private void initListener() {
        bt_newgroup_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //调转页面
                Intent intent=new Intent(NewGroupActivity.this,PickContactActivity.class);

                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //成功获取联系人
        if (resultCode==RESULT_OK){
            createGroup(data.getStringArrayExtra("memberses"));
        }
    }

    private  void createGroup(final String[] memberses){
        //群名称
        final String groupName=et_newgroup_name.getText().toString();
        //群描述
        final String groupDesc=et_newgroup_desc.getText().toString();
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {





                //去环信服务器创建群
                EMGroupOptions options=new EMGroupOptions();
                //群最多容纳的人数
                options.maxUsers=200;
                EMGroupManager.EMGroupStyle groupStyle=null;
                if (cb_newgroup_public.isChecked()){ //公开
                    if (cb_newgroup_invite.isChecked()){//开放邀请
                        groupStyle=EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    }else {
                        groupStyle=EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                }else{//不公开
                    if (cb_newgroup_invite.isChecked()){//开放邀请
                        groupStyle=EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    }else {
                        groupStyle=EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                }
                //创建群的类型
                options.style=groupStyle;
                //1 群名称,2 群描述 ,3群成员, 4原因 5.参数设置
                try {
                    EMClient.getInstance().groupManager().createGroup(groupName,groupDesc,memberses,"申请加入",options);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this,"创建群成功",Toast.LENGTH_SHORT).show();
                            //结束当前页面
                            finish();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this,"创建失败",Toast.LENGTH_SHORT).show();
                            //结束当前页面
                            finish();
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        et_newgroup_name=findViewById(R.id.et_newgroup_name);
        et_newgroup_desc=findViewById(R.id.et_newgroup_desc);
        cb_newgroup_public=findViewById(R.id.cb_newgroup_public);
        cb_newgroup_invite=findViewById(R.id.cb_newgroup_invite);
        bt_newgroup_create=findViewById(R.id.bt_newgroup_create);
    }
}
