package com.example.imsapplication.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.imsapplication.R;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 吕文杰 on 2018/4/20.
 */

public class GroupListAdapter extends BaseAdapter {
    private Context mContext;
    private List<EMGroup> emGroups=new ArrayList<>();
    public GroupListAdapter(Context context){
        this.mContext=context;
    }
    //刷新方法
    public void refresh(List<EMGroup> groups){
        if (groups!=null&&groups.size()>=0)
        emGroups.clear();
        emGroups.addAll(groups);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return emGroups==null?0:emGroups.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if (view==null){
            holder=new ViewHolder();
            view=View.inflate(mContext, R.layout.item_grouplist,null);
            holder.name=view.findViewById(R.id.tv_grouplist_name);
            view.setTag(holder);

        }else{
            holder=(ViewHolder)view.getTag();
        }
        //获取当前的数据
        EMGroup emGroup=emGroups.get(i);
        //显示数据
        holder.name.setText(emGroup.getGroupName());

        return view;
    }
    private class ViewHolder{
         TextView name;
    }
}
