package com.example.imsapplication.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.example.imsapplication.R;
import com.example.imsapplication.controller.adapter.GroupListAdapter;
import com.example.imsapplication.modle.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * Created by 吕文杰 on 2018/4/20.
 */

public class GroupListActivity extends Activity {
    private ListView lv_grouplist;
    private GroupListAdapter groupListAdapter;
    private LinearLayout ll_grouplist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        initView();
        initData();
        initListener();

    }

    private void initListener() {
        //listView的item的点击事件
        lv_grouplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    return;
                }
                Intent intent=new Intent(GroupListActivity.this,ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);
                //群id
                EMGroup emGroup=EMClient.getInstance().groupManager().getAllGroups().get(i-1);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,emGroup.getGroupId());
                startActivity(intent);
            }
        });
        //跳转到新建群页面
        ll_grouplist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GroupListActivity.this,NewGroupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        groupListAdapter=new GroupListAdapter(this );
        lv_grouplist.setAdapter(groupListAdapter);
        //从环信上获取数据
        getGroupFromServer();
    }
    //从环信上获取数据
    private void getGroupFromServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //从网络获取
                try {
                    List<EMGroup> mGroup= EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    //更新页面
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupListActivity.this,"加载数据成功",Toast.LENGTH_SHORT).show();
                            refresh();
                        }
                    });



                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //刷新方法
    private void refresh() {
        groupListAdapter.refresh(EMClient.getInstance().groupManager().getAllGroups());
    }

    private void initView() {
        lv_grouplist=(ListView) findViewById(R.id.lv_grouplist);
        //添加头布局
        View headView=View.inflate(this,R.layout.header_grouplist,null);
        lv_grouplist.addHeaderView(headView);
        ll_grouplist=(LinearLayout)headView.findViewById(R.id.ll_grouplist);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新一下页面
        refresh();
    }
}
