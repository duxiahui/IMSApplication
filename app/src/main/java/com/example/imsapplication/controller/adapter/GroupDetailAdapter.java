package com.example.imsapplication.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imsapplication.R;
import com.example.imsapplication.modle.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 吕文杰 on 2018/4/28.
 */

public class GroupDetailAdapter extends BaseAdapter {
    private Context mContext;
    private boolean mIsCanModfy;//是否允许添加或删除群成员
    private List<UserInfo> mUsers=new ArrayList<>();
    public boolean mIsDeleteModel;//删除模式   true:可以 false:不可以

    public GroupDetailAdapter(Context context,boolean mIsCanModfy){
        this.mContext=context;
        this.mIsCanModfy=mIsCanModfy;

    }
    //获取当前的删除模式
    public boolean ismIsDeleteModel(){
        return mIsDeleteModel;
    }
    //设置当前的删除模式
    public void setIsDeleteModel(boolean mIsDeleteModel){
        this.mIsDeleteModel=mIsDeleteModel;
    }
    public void refresh(List<UserInfo> users){
        if (users!=null&&users.size()>=0){
            mUsers.clear();
            //添加加减号
            initUsers();
            mUsers.addAll(users);
        }
        notifyDataSetChanged();

    }

    private void initUsers() {
        UserInfo add=new UserInfo("add");
        UserInfo delete=new UserInfo("delete");
        mUsers.add(add);
        mUsers.add(delete);
    }

    @Override
    public int getCount() {
        return mUsers==null?0:mUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return mUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (view==null){
            viewHolder=new ViewHolder();
            view=View.inflate(mContext, R.layout.item_groupdetail,null);
            viewHolder.photo=view.findViewById(R.id.iv_group_detail_photo);
            viewHolder.delete=view.findViewById(R.id.iv_group_detail_delete);
            viewHolder.name=view.findViewById(R.id.tv_group_detail_name);
            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)view.getTag();
        }
        //获取当前item的数据
        UserInfo userInfo=mUsers.get(position);
        //显示数据
        if (mIsCanModfy){//群主开放权限
            //布局处理
            if (position==getCount()-1){//减号的处理
                if (mIsDeleteModel){
                    view.setVisibility(View.INVISIBLE);
                }else{
                    view.setVisibility(View.VISIBLE);
                    viewHolder.photo.setImageResource(R.drawable.em_smiley_minus_btn_pressed);
                    viewHolder.name.setVisibility(View.INVISIBLE);
                    viewHolder.delete.setVisibility(View.GONE);
                }

            }else if(position==getCount()-2){//加号的处理
                if (mIsDeleteModel){
                    view.setVisibility(View.INVISIBLE);
                }else{
                    view.setVisibility(View.VISIBLE);
                    viewHolder.photo.setImageResource(R.drawable.em_smiley_add_btn_pressed);
                    viewHolder.name.setVisibility(View.INVISIBLE);
                    viewHolder.delete.setVisibility(View.GONE);
                }
            }else{//群成员
                view.setVisibility(View.VISIBLE);
                viewHolder.name.setVisibility(View.VISIBLE);
                viewHolder.name.setText(userInfo.getName());
                viewHolder.photo.setImageResource(R.drawable.em_default_avatar);
                if (mIsDeleteModel){
                    viewHolder.delete.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.delete.setVisibility(View.GONE);
                }

            }


            //点击事件的处理


        }else{//普通成员
            if(position==getCount()-1||position==getCount()-2){
                view.setVisibility(View.GONE);
            }else {
                view.setVisibility(View.VISIBLE);
                viewHolder.name.setText(userInfo.getName());
                viewHolder.photo.setImageResource(R.drawable.em_default_avatar);
                viewHolder.delete.setVisibility(View.GONE);
            }

        }


        return view;
    }
    private  class ViewHolder{
        private ImageView photo;
        private ImageView delete;
        private TextView name;
    }
}
