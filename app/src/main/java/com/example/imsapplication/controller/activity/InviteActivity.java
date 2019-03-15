package com.example.imsapplication.controller.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;


import com.example.imsapplication.R;
import com.example.imsapplication.controller.adapter.InviteAdapter;
import com.example.imsapplication.modle.Model;
import com.example.imsapplication.modle.bean.InvationInfo;
import com.example.imsapplication.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * Created by 吕文杰 on 2018/4/18.
 */

public class InviteActivity extends Activity {
    private ListView lv_invite;
    private InviteAdapter inviteAdapter;

    private InviteAdapter.OnInviteLister mOnInviteLister=new InviteAdapter.OnInviteLister() {
        @Override
        public void onAccpet(final InvationInfo invationInfo) {
                //通知环信服务器,点击接受了邀请
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().acceptInvitation(invationInfo.getUser().getHxid());
                        //更新数据库
                        Model.getInstance().getDbManager().getinviteTableDao().updateInvitationStatus(InvationInfo.InvitationStatus.INVITE_ACCEPT,invationInfo.getUser().getHxid());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面变化
                                Toast.makeText(InviteActivity.this,"接受邀请成功",Toast.LENGTH_SHORT).show();
                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面变化
                                Toast.makeText(InviteActivity.this,"接受邀请失败",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onReject(final InvationInfo invationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().declineInvitation(invationInfo.getUser().getHxid());
                        //更新数据库
                        Model.getInstance().getDbManager().getinviteTableDao().removeInvitation(invationInfo.getUser().getHxid());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面变化
                                Toast.makeText(InviteActivity.this,"拒绝成功了",Toast.LENGTH_SHORT).show();
                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面变化
                                Toast.makeText(InviteActivity.this,"拒绝失败",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onInviteAccpet(final InvationInfo invationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //告诉环信服务器接受了邀请
                        EMClient.getInstance().groupManager().acceptInvitation(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvitePerson());
                        //更新数据库
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_ACCEPT_INVITE);
                        Model.getInstance().getDbManager().getinviteTableDao().addInvitation(invationInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面变化
                                Toast.makeText(InviteActivity.this,"接受邀请",Toast.LENGTH_SHORT).show();
                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面变化
                                Toast.makeText(InviteActivity.this,"接受失败",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onInviteReject(final InvationInfo invationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //告诉环信服务器接受了邀请
                        EMClient.getInstance().groupManager().declineInvitation(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvitePerson(),"拒绝邀请");
                        //更新数据库
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_REJECT_INVITE);
                        Model.getInstance().getDbManager().getinviteTableDao().addInvitation(invationInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面变化
                                Toast.makeText(InviteActivity.this,"拒绝邀请",Toast.LENGTH_SHORT).show();
                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面变化
                                Toast.makeText(InviteActivity.this,"拒绝失败",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onApplicationAccpet(final InvationInfo invationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //告诉环信服务器接受了申请
                        EMClient.getInstance().groupManager().acceptApplication(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvitePerson());
                        //更新数据库
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION);
                        Model.getInstance().getDbManager().getinviteTableDao().addInvitation(invationInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面变化
                                Toast.makeText(InviteActivity.this,"接受申请",Toast.LENGTH_SHORT).show();
                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面变化
                                Toast.makeText(InviteActivity.this,"接受失败",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onApplicationReject(final InvationInfo invationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //告诉环信服务器拒绝了申请
                        EMClient.getInstance().groupManager().declineApplication(invationInfo.getGroup().getGroupId(),invationInfo.getGroup().getInvitePerson(),"拒绝申请");
                        //更新数据库
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_REJECT_APPLICATION);
                        Model.getInstance().getDbManager().getinviteTableDao().addInvitation(invationInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面变化
                                Toast.makeText(InviteActivity.this,"拒绝申请",Toast.LENGTH_SHORT).show();
                                //刷新页面
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面变化
                                Toast.makeText(InviteActivity.this,"拒绝失败",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });
        }
    };
    private BroadcastReceiver InviteChangedReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新界面
            refresh();;
        }
    };
    private LocalBroadcastManager mLBM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        initView();
        initData();
    }

    private void initData() {
        inviteAdapter=new InviteAdapter(this,mOnInviteLister);
        lv_invite.setAdapter(inviteAdapter);
        refresh();

        //注册邀请信息变化的广播
        mLBM=LocalBroadcastManager.getInstance(this);
        mLBM.registerReceiver(InviteChangedReceiver,new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        mLBM.registerReceiver(InviteChangedReceiver,new IntentFilter(Constant.GROUP_INVITE_CHANGED));
    }
    //刷新页面
    private void refresh() {
        //获取数据库中所有的邀请信息列表
        List<InvationInfo> ins=Model.getInstance().getDbManager().getinviteTableDao().getInvitations();

        Log.e("------:",ins.toString());
        //刷新适配器
        inviteAdapter.refresh(ins);

    }

    private void initView() {

        lv_invite=(ListView) findViewById(R.id.lv_intive);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLBM.unregisterReceiver(InviteChangedReceiver);
    }
}
