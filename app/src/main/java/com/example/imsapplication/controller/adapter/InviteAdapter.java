package com.example.imsapplication.controller.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.imsapplication.R;
import com.example.imsapplication.modle.bean.InvationInfo;
import com.example.imsapplication.modle.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 吕文杰 on 2018/4/18.
 */

public class InviteAdapter extends BaseAdapter {
    private Context mContext;
    private List<InvationInfo> mInvationInfos=new ArrayList<>();
    private InvationInfo invationInfo;
    private OnInviteLister mOnInviteLister;
    public InviteAdapter(Context context,OnInviteLister onInviteLister) {
        this.mContext=context;
        mOnInviteLister=onInviteLister;
    }
    //数据刷新的方法
    public void refresh(List<InvationInfo> invationInfos){
            if (invationInfos !=null&& invationInfos.size()>=0){
                mInvationInfos.clear();
                Log.e("传递数据",invationInfos.toString());
                mInvationInfos.addAll(invationInfos);
                notifyDataSetChanged();
            }
    }

    @Override
    public int getCount() {
        return mInvationInfos==null?0:mInvationInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mInvationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //获取viewHolder
        ViewHodler hodler=null;
        if (view==null){
            hodler=new ViewHodler();
            view=View.inflate(mContext, R.layout.item_invite,null);
            hodler.name=(TextView) view.findViewById(R.id.tv_invite_name);
            hodler.reason=(TextView) view.findViewById(R.id.tv_invite_reason);
            hodler.accept=(Button) view.findViewById(R.id.bt_invite_accept);
            hodler.reject=(Button) view.findViewById(R.id.bt_invite_reject);
            view.setTag(hodler);


        }else {
            hodler=(ViewHodler)view.getTag();
        }
        //获取当前Item数据
        invationInfo=mInvationInfos.get(position);
        //显示当前数据
        UserInfo userInfo=invationInfo.getUser();
        if (userInfo!=null){//联系人
            //名称展示
            hodler.name.setText(invationInfo.getUser().getName());
            hodler.accept.setVisibility(View.GONE);
            hodler.reject.setVisibility(View.GONE);
            //原因
            if (invationInfo.getStatus()==InvationInfo.InvitationStatus.NEW_INVITE){//新的邀请
                if (invationInfo.getReason()==null){
                    hodler.reason.setText("添加好友");
                }else {
                    hodler.reason.setText(invationInfo.getReason());
                }
                hodler.accept.setVisibility(View.VISIBLE);
                hodler.reject.setVisibility(View.VISIBLE);
            }else if (invationInfo.getStatus()==InvationInfo.InvitationStatus.INVITE_ACCEPT){//接受邀请
                if (invationInfo.getReason()==null){
                    hodler.reason.setText("接受邀请");
                }else{
                    hodler.reason.setText(invationInfo.getReason());
                }
            }else if (invationInfo.getStatus()==InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER){//邀请被接受后
                if (invationInfo.getReason()==null){
                    hodler.reason.setText("邀请被接受");
                }else{
                    hodler.reason.setText(invationInfo.getReason());
                }

            }

            //同意按钮处理
            hodler.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnInviteLister.onAccpet(invationInfo);
                }
            });
            //拒绝的按钮处理
            hodler.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnInviteLister.onReject(invationInfo);
                }
            });

        }else {//群组
                //显示名称
            hodler.name.setText(invationInfo.getGroup().getInvitePerson());
            hodler.accept.setVisibility(View.GONE);
            hodler.reject.setVisibility(View.GONE);
            //原因
            switch (invationInfo.getStatus()){

                case GROUP_APPLICATION_ACCEPTED:
                    hodler.reason.setText("您的群申请以被接受");
                    break;
                case GROUP_INVITE_ACCEPTED:
                    hodler.reason.setText("您的群邀请以被接受");
                    break;
                case GROUP_APPLICATION_DECLINED:
                    hodler.reason.setText("您的群申请以被拒绝");
                    break;
                case GROUP_INVITE_DECLINED:
                    hodler.reason.setText("您的群申请以被拒绝");
                    break;
                //接受到了群邀请
                case NEW_GROUP_INVITE:
                    hodler.accept.setVisibility(View.VISIBLE);
                    hodler.reject.setVisibility(View.VISIBLE);
                    //接受邀请
                    hodler.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOnInviteLister.onInviteAccpet(invationInfo);
                        }
                    });

                    //拒绝邀请
                    hodler.reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOnInviteLister.onInviteReject(invationInfo);
                        }
                    });
                    hodler.reason.setText("您收到了群邀请");
                    break;
                //您收到了群申请
                case NEW_GROUP_APPLICATION:
                    hodler.accept.setVisibility(View.VISIBLE);
                    hodler.reject.setVisibility(View.VISIBLE);
                    //接受申请
                    hodler.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOnInviteLister.onApplicationAccpet(invationInfo);
                        }
                    });

                    //拒绝申请
                    hodler.reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOnInviteLister.onApplicationReject(invationInfo);
                        }
                    });
                    hodler.reason.setText("您收到了群申请");
                    break;
                //您接受了群邀请
                case GROUP_ACCEPT_INVITE:

                    hodler.reason.setText("您接受了群邀请");
                    break;
                //您接受了群申请
                case GROUP_ACCEPT_APPLICATION:

                    hodler.reason.setText("您接受群申请");
                    break;
                //您拒绝群申请
                case GROUP_REJECT_INVITE:

                    hodler.reason.setText("您拒绝群申请");
                    break;
                //您拒绝群申请
                case GROUP_REJECT_APPLICATION:

                    hodler.reason.setText("您拒绝群申请");
                    break;

            }

        }




        return view;
    }
    private class ViewHodler{
        private TextView name;
        private TextView reason;
        private Button accept;
        private Button reject;
    }
    public interface OnInviteLister{

        //联系人接受按钮的点击事件
        void onAccpet(InvationInfo invationInfo);
        //联系人拒绝按钮的点击事件
        void onReject(InvationInfo invationInfo);
        //接受邀请按钮的点击事件
        void onInviteAccpet(InvationInfo invationInfo);
        //拒绝邀请按钮的点击事件
        void onInviteReject(InvationInfo invationInfo);
        //接受申请按钮的点击事件
        void onApplicationAccpet(InvationInfo invationInfo);
        //拒绝申请按钮的点击事件
        void onApplicationReject(InvationInfo invationInfo);
    }

}
