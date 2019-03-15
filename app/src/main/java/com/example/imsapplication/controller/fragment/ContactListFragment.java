package com.example.imsapplication.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.imsapplication.R;
import com.example.imsapplication.controller.activity.AddContactActivity;
import com.example.imsapplication.controller.activity.ChatActivity;
import com.example.imsapplication.controller.activity.GroupListActivity;
import com.example.imsapplication.controller.activity.InviteActivity;
import com.example.imsapplication.modle.Model;
import com.example.imsapplication.modle.bean.UserInfo;
import com.example.imsapplication.utils.Constant;
import com.example.imsapplication.utils.SpUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 吕文杰 on 2018/4/12.
 */

public class ContactListFragment extends EaseContactListFragment {
    private ImageView iv_contact_red;
    private LinearLayout ll_contact_invite;
    private LocalBroadcastManager mLBM;
    private  String mHxid;

    private BroadcastReceiver ContactChangedReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新页面
            refreshContact();
        }
    };
    private BroadcastReceiver ContactInviteChangedReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新红点显示
            iv_contact_red.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
        }
    };
    private BroadcastReceiver GroupChangeReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新红点显示
            iv_contact_red.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
        }
    };
    @Override
    protected void initView() {
        super.initView();
        //布局添加加号+
        titleBar.setRightImageResource(R.drawable.em_add);

        //添加头布局
        View headerView=View.inflate(getActivity(),R.layout.header_add_contact,null);
        listView.addHeaderView(headerView);
        //获取红点对象
        iv_contact_red=(ImageView) headerView.findViewById(R.id.iv_contact_red);
        //获取邀请条目的对象
        ll_contact_invite=(LinearLayout) headerView.findViewById(R.id.ll_contact_invite);
        //设置listview条目的点击事件
        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                if (user==null){
                    return;
                }
                //跳转到会话页面
                Intent intent=new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,user.getUsername());
                startActivity(intent);


            }
        });
        //跳转到群组列表页面
        LinearLayout ll_contact_group=headerView.findViewById(R.id.ll_contact_group);
        ll_contact_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), GroupListActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //获取环信ID
        int position=((AdapterView.AdapterContextMenuInfo)menuInfo).position;
        EaseUser easeUser=(EaseUser)listView.getItemAtPosition(position);
         mHxid=easeUser.getUsername();
        //添加布局
        getActivity().getMenuInflater().inflate(R.menu.delete,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.contact_delete){
            //执行删除操作
                deleteContact();
            return true;
        }
        return  super.onContextItemSelected(item);
    }
    //执行删除选中联系人操作
    private void deleteContact() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //从环信服务器中删除联系人
                try {
                    EMClient.getInstance().contactManager().deleteContact(mHxid);
                    //本地数据库需要更新
                    Model.getInstance().getDbManager().getContactTabDao().deleteContactByHxId(mHxid);

                    if (getActivity()==null){
                        return;
                    }
                            //更新ui
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //toast显示
                                    Toast.makeText(getActivity(),"删除"+mHxid+"成功",Toast.LENGTH_SHORT).show();
                                    //刷新页面
                                    refreshContact();
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            //更新ui
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                            //toast显示
                            Toast.makeText(getActivity(), "删除" + mHxid + "失败", Toast.LENGTH_SHORT).show();


                        }
                    });
                }
            }
        });
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        //添加联系人按钮的点击事件处理
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AddContactActivity.class);
                startActivity(intent);
            }
        });

        //SP拿去红点标记的判断  初始化红点显示
        boolean isNewInvite= SpUtils.getInstance().getBoolean(SpUtils.IS_NEW_INVITE,false);
        iv_contact_red.setVisibility(isNewInvite?View.VISIBLE:View.GONE);


        //邀请信息的点击事件
        ll_contact_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //红点要处理
                iv_contact_red.setVisibility(View.GONE);
                SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,false);
                //跳转到邀请的列表信息页面
                Intent intent=new Intent(getActivity(), InviteActivity.class);
                startActivity(intent);
            }
        });
        //注册广播
        mLBM=LocalBroadcastManager.getInstance(getActivity());
        mLBM.registerReceiver(ContactChangedReceiver,new IntentFilter(Constant.CONTACT_CHANGED));
        mLBM.registerReceiver(ContactInviteChangedReceiver,new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        mLBM.registerReceiver(GroupChangeReceiver,new IntentFilter(Constant.GROUP_INVITE_CHANGED));

        //从环信服务器获取所有联系人的信息
        getContactFromHxServer();
        //绑定listview和contextmenu
        registerForContextMenu(listView);
    }
    //从环信服务器获取所有联系人的信息
    private void getContactFromHxServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //获取到所有的好友的环信ID
                try {
                    List<String> hxIds= EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Log.e("1111111111",hxIds.toString());
                    //校验
                    if (hxIds!=null&&hxIds.size()>=0){
                        List<UserInfo> contacts=new ArrayList<UserInfo>();
                        //转换
                        for (String hxId:hxIds){
                            UserInfo userInfo=new UserInfo(hxId);
                            contacts.add(userInfo);
                        }
                        //保存到好友信息的本地数据库中
                        Model.getInstance().getDbManager().getContactTabDao().savaContacts(contacts,true);
                        if (getActivity()==null){
                            return;
                        }
                        //刷新页面
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //刷新页面的方法
                                refreshContact();
                            }
                        });
                    }


                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //刷新页面的方法
    private void refreshContact() {
        //获取数据
        List<UserInfo> contacts=Model.getInstance().getDbManager().getContactTabDao().getContacts();

        //校验
        if (contacts!=null&&contacts.size()>=0){
        //设置数据
            Map<String,EaseUser> contactsMap=new HashMap<>();
            //转化
            for(UserInfo contact:contacts){
                EaseUser easeUser=new EaseUser(contact.getName());
                contactsMap.put(contact.getName(),easeUser);
            }
            setContactsMap(contactsMap);
        }
        //刷新页面
        refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLBM.unregisterReceiver(ContactChangedReceiver);
        mLBM.unregisterReceiver(ContactInviteChangedReceiver);
        mLBM.unregisterReceiver(ContactChangedReceiver);
    }

    
}
