package com.example.imsapplication;

import android.app.Application;
import android.content.Context;


import com.example.imsapplication.modle.Model;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;


/**
 * Created by 吕文杰 on 2018/4/10.
 */

public class IMApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化EaseUI
        EMOptions  options=new EMOptions();
        options.setAcceptInvitationAlways(false);//设置同意后才能接受邀请
        options.setAutoAcceptGroupInvitation(false);//设置同意后才能接受群邀请
        EaseUI.getInstance().init(this,options);
        //初始化数据模型类
        Model.getInstance().init(this);

        //初始化全局上下文
        mContext=this;
    }
    //获取全局上下文的对象
    public static Context getGlobalApplication (){
        return mContext;
    }
}
