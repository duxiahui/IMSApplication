package com.example.imsapplication.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.imsapplication.R;
import com.example.imsapplication.controller.adapter.PickContactAdapter;
import com.example.imsapplication.modle.Model;
import com.example.imsapplication.modle.bean.PickContactInfo;
import com.example.imsapplication.modle.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 吕文杰 on 2018/4/27.
 */

public class PickContactActivity extends Activity {
    private TextView tv_pick_save;
    private ListView lv_pick;
    private List<PickContactInfo> mPicks;
    private PickContactAdapter pickContactAdapter;
    private List<String> mExistMembers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contact);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        lv_pick.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //checkBox的转换
                CheckBox cb=view.findViewById(R.id.cb_pick);
                cb.setChecked(!cb.isChecked());
                //修改数据
                PickContactInfo pi=mPicks.get(i);
                pi.setChecked(cb.isChecked());
                //刷新页面
                pickContactAdapter.notifyDataSetChanged();

            }
        });
        //保存按钮
        tv_pick_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取到已经选择的联系人
                List<String> names=pickContactAdapter.getPickContacts();

                Intent in=new Intent();
                in.putExtra("memberses", names.toArray(new String[0]));
                //设置返回密码
                setResult(RESULT_OK,in);
                finish();
            }
        });
    }

    private void initData() {
        //从本地数据库列表中获取数据
        List<UserInfo> contacts= Model.getInstance().getDbManager().getContactTabDao().getContacts();
        mPicks=new ArrayList<>();

        if (contacts!=null&&contacts.size()>=0){
            for (UserInfo user:contacts){
                PickContactInfo pickContactInfo=new PickContactInfo(user,false);
                mPicks.add(pickContactInfo);
            }
        }
        pickContactAdapter=new PickContactAdapter(this,mPicks,mExistMembers);
        lv_pick.setAdapter(pickContactAdapter);


    }

    private void initView() {
        tv_pick_save=findViewById(R.id.tv_pick_save);
        lv_pick=findViewById(R.id.lv_pick);
    }
}
