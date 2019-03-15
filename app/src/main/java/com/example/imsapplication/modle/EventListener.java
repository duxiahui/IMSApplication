package com.example.imsapplication.modle;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;


import com.example.imsapplication.modle.bean.GroupInfo;
import com.example.imsapplication.modle.bean.InvationInfo;
import com.example.imsapplication.modle.bean.UserInfo;
import com.example.imsapplication.utils.Constant;
import com.example.imsapplication.utils.SpUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMucSharedFile;

import java.util.List;

/**
 * Created by 吕文杰 on 2018/4/17.
 */

public class EventListener {
    private Context mContext;
    private  LocalBroadcastManager mLBM;

    public EventListener(Context context){
        mContext=context;
            //创建一个发送广播的管理对象
        mLBM=LocalBroadcastManager.getInstance(mContext);

        //注册一个联系人变化的监听事件
        EMClient.getInstance().contactManager().setContactListener(emContactListener);
        //注册群信息变化的监听事件
     //   EMClient.getInstance().groupManager().addGroupChangeListener(emGroupChangeListener);
    }
    //注册群信息变化的监听事件
//    private  final EMGroupChangeListener emGroupChangeListener=new EMGroupChangeListener() {
//        //收到群邀请
//        @Override
//        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
//            //更新数据库
//            InvationInfo invationInfo=new InvationInfo();
//            invationInfo.setReason(reason);
//            invationInfo.setGroup(new GroupInfo(groupName,groupId,inviter));
//            invationInfo.setStatus(InvationInfo.InvitationStatus.NEW_GROUP_INVITE);
//            Model.getInstance().getDbManager().getinviteTableDao().addInvitation(invationInfo);
//            //红点的处理
//            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
//            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
//
//        }
//
//        @Override
//        public void onRequestToJoinReceived(String s, String s1, String s2, String s3) {
//
//        }
//
//        @Override
//        public void onRequestToJoinAccepted(String s, String s1, String s2) {
//
//        }
//
//        @Override
//        public void onRequestToJoinDeclined(String s, String s1, String s2, String s3) {
//
//        }
//
//        //接受群申请的通知
//        @Override
//        public void onApplicationReceived(String groupId, String groupName, String inviter, String reason) {
//            //数据库更新
//            InvationInfo invationInfo=new InvationInfo();
//            invationInfo.setReason(reason);
//            invationInfo.setGroup(new GroupInfo(groupName,groupId,inviter));
//            invationInfo.setStatus(InvationInfo.InvitationStatus.NEW_GROUP_APPLICATION);
//            Model.getInstance().getDbManager().getinviteTableDao().addInvitation(invationInfo);
//            //红点的处理
//            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
//            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
//
//
//        }
//        //接受 群申请被接受
//        @Override
//        public void onApplicationAccept(String groupId, String groupName, String accepter) {
//            //数据库更新
//            InvationInfo invationInfo=new InvationInfo();
//            invationInfo.setGroup(new GroupInfo(groupName,groupId,accepter));
//            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED);
//            //红点的处理
//            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
//            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
//        }
//        //收到 群申请被拒绝
//        @Override
//        public void onApplicationDeclined(String groupId, String groupName, String inviter, String reason) {
//
//            //数据库更新
//            InvationInfo invationInfo=new InvationInfo();
//            invationInfo.setReason(reason);
//            invationInfo.setGroup(new GroupInfo(groupName,groupId,inviter));
//            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED);
//            Model.getInstance().getDbManager().getinviteTableDao().addInvitation(invationInfo);
//            //红点的处理
//            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
//            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
//
//        }
//        //收到 群邀请被同意
//        @Override
//        public void onInvitationAccepted(String groupId, String inviter, String reason) {
//            //数据库更新
//            InvationInfo invationInfo=new InvationInfo();
//            invationInfo.setReason(reason);
//            invationInfo.setGroup(new GroupInfo(groupId,groupId,inviter));
//            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);
//            Model.getInstance().getDbManager().getinviteTableDao().addInvitation(invationInfo);
//            //红点的处理
//            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
//            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
//        }
//        //收到群邀请被拒绝
//        @Override
//        public void onInvitationDeclined(String groupId, String inviter, String reason) {
//            //数据库更新
//            InvationInfo invationInfo=new InvationInfo();
//            invationInfo.setReason(reason);
//            invationInfo.setGroup(new GroupInfo(groupId,groupId,inviter));
//            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_DECLINED);
//            Model.getInstance().getDbManager().getinviteTableDao().addInvitation(invationInfo);
//            //红点的处理
//            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
//            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
//        }
//        //收到群成员被删除
//        @Override
//        public void onUserRemoved(String s, String s1) {
//
//        }
//        //收到 群被解散
//        @Override
//        public void onGroupDestroyed(String s, String s1) {
//
//        }
//        //收到 群邀请被自动接受
//        @Override
//        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String reason) {
////数据库更新
//            InvationInfo invationInfo=new InvationInfo();
//            invationInfo.setReason(reason);
//            invationInfo.setGroup(new GroupInfo(groupId,groupId,inviter));
//            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);
//            Model.getInstance().getDbManager().getinviteTableDao().addInvitation(invationInfo);
//            //红点的处理
//            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
//            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
//        }
//
//        @Override
//        public void onMuteListAdded(String s, List<String> list, long l) {
//
//        }
//
//        @Override
//        public void onMuteListRemoved(String s, List<String> list) {
//
//        }
//
//        @Override
//        public void onAdminAdded(String s, String s1) {
//
//        }
//
//        @Override
//        public void onAdminRemoved(String s, String s1) {
//
//        }
//
//        @Override
//        public void onOwnerChanged(String s, String s1, String s2) {
//
//        }
//
//        @Override
//        public void onMemberJoined(String s, String s1) {
//
//        }
//
//        @Override
//        public void onMemberExited(String s, String s1) {
//
//        }
//
//        @Override
//        public void onAnnouncementChanged(String s, String s1) {
//
//        }
//
//        @Override
//        public void onSharedFileAdded(String s, EMMucSharedFile emMucSharedFile) {
//
//        }
//
//        @Override
//        public void onSharedFileDeleted(String s, String s1) {
//
//        }
//    };


    //注册一个联系人的监听事件
    private final EMContactListener emContactListener=new EMContactListener() {
        //联系人增加后执行的方法
        @Override
        public void onContactAdded(String hxId) {
              //数据更新
            Model.getInstance().getDbManager().getContactTabDao().savaContact(new UserInfo(hxId),true);
            //发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }
        //联系人删除后执行的方法
        @Override
        public void onContactDeleted(String hxId) {
            //数据更新
            Model.getInstance().getDbManager().getContactTabDao().deleteContactByHxId(hxId);
            Model.getInstance().getDbManager().getinviteTableDao().removeInvitation(hxId);

            //发送一条联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }
        //接受到联系人新的邀请
        @Override
        public void onContactInvited(String hxId, String reason) {
            //更新数据库
            InvationInfo invationInfo=new InvationInfo();
            invationInfo.setUser(new UserInfo(hxId));
            invationInfo.setReason(reason);
            invationInfo.setStatus(InvationInfo.InvitationStatus.NEW_INVITE);//新的邀请
            Model.getInstance().getDbManager().getinviteTableDao().addInvitation(invationInfo);

            //处理红点的
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        @Override
        public void onFriendRequestAccepted(String s) {
            //更新数据库
            InvationInfo invationInfo=new InvationInfo();
            invationInfo.setUser(new UserInfo(s));
            invationInfo.setStatus(InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);//别人同意了你的邀请
            Model.getInstance().getDbManager().getinviteTableDao().addInvitation(invationInfo);
            //处理红点的
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        @Override
        public void onFriendRequestDeclined(String s) {
            //处理红点的
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        //别人同意了你的好友邀请

    };

}
