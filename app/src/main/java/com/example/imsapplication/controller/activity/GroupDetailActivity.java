package com.example.imsapplication.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;


import com.example.imsapplication.R;
import com.example.imsapplication.controller.adapter.GroupDetailAdapter;
import com.example.imsapplication.modle.Model;
import com.example.imsapplication.modle.bean.UserInfo;
import com.example.imsapplication.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 吕文杰 on 2018/4/28.
 */

public class GroupDetailActivity extends Activity {
    private EMGroup emGroup;
    private Button bt_groupdetail_out;
    private GridView gv_groupdatil;
    private List<UserInfo> mUsers;
    private GroupDetailAdapter groupDetailAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        initView();
        getData();
        initData();
        initListener();
    }

    private void initListener() {
        gv_groupdatil.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //判断当前是否是删除模式
                        if (groupDetailAdapter.mIsDeleteModel){
                            //切换非删除模式
                            groupDetailAdapter.setIsDeleteModel(false);
                            groupDetailAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {
        //初始化BUTTON
        initButtonDisplay();
        //初始化GridView
        initGridView();
        //从环信服务器获取所有的群成员
        getMembersFromHxServer();
    }

    private void getMembersFromHxServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //从环信服务器获取所有的群成员
                try {
                    EMGroup mGroup=EMClient.getInstance().groupManager().getGroupFromServer(emGroup.getGroupId());
                    List<String> members=mGroup.getMembers();
                    if (members!=null&&members.size()>=0){
                        mUsers=new ArrayList<UserInfo>();
                        for(String member:members){
                            UserInfo user=new UserInfo(member);
                            mUsers.add(user);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            groupDetailAdapter.refresh(mUsers);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupDetailActivity.this,"获取群信息失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initGridView() {
        //判断是否是群主||群公开
        boolean isCanModify=EMClient.getInstance().getCurrentUser().equals(emGroup.getOwner())||emGroup.isPublic();
        groupDetailAdapter=new GroupDetailAdapter(this,isCanModify);
        gv_groupdatil.setAdapter(groupDetailAdapter);

    }

    private void initButtonDisplay() {
        //判断当前用户是否是群主
        if (EMClient.getInstance().getCurrentUser().equals(emGroup.getOwner())){
            bt_groupdetail_out.setText("解散群");
            bt_groupdetail_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            //去环信解散群
                            try {
                                EMClient.getInstance().groupManager().destroyGroup(emGroup.getGroupId());
                                //发送退群的广播
                                exitGroupBroadCast();
                                //更新UI
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this,"解散群成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                                
                                
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                //更新UI
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this,"解散群失败",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }else{//群成员
            bt_groupdetail_out.setText("退群");
            bt_groupdetail_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //去环信服务器退群
                    try {
                        EMClient.getInstance().groupManager().leaveGroup(emGroup.getGroupId());
                        //发送退群的广播
                        exitGroupBroadCast();
                        //更新UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this,"退群成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        //更新UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this,"退群失败",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }

                }
            });

        }
    }

    private void exitGroupBroadCast() {
        LocalBroadcastManager mLBM=LocalBroadcastManager.getInstance(GroupDetailActivity.this);
        Intent intent=new Intent(Constant.EXIT_GROUP);
        intent.putExtra(Constant.GROUP_ID,emGroup.getGroupId());
        mLBM.sendBroadcast(intent);

    }


    private void getData(){
        String groupId=getIntent().getStringExtra(Constant.GROUP_ID);
        if (groupId==null){
            return;
        }else {
            emGroup= EMClient.getInstance().groupManager().getGroup(groupId);
        }


    }
    private void initView() {
        bt_groupdetail_out=findViewById(R.id.bt_groupdetail_out);
        gv_groupdatil=findViewById(R.id.gv_groupdatil);
    }

    
}
