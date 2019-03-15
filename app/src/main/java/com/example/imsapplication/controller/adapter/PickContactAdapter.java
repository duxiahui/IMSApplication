package com.example.imsapplication.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.imsapplication.R;
import com.example.imsapplication.modle.bean.PickContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 吕文杰 on 2018/4/27.
 */

public class PickContactAdapter extends BaseAdapter {
    private Context mContext;
    private List<PickContactInfo> mPicks=new ArrayList<>();
    private List<String>  mExistMembers=new ArrayList<>();
    public PickContactAdapter(Context context,List<PickContactInfo> picks,List<String> existMembers){
        this.mContext=context;
        if (picks!=null&&picks.size()>=0){
            mPicks.clear();;
            mPicks.addAll(picks);
        }
        if (existMembers!=null&&existMembers.size()>=0){
            mExistMembers.clear();;
            mExistMembers.addAll(existMembers);
        }
    }
    @Override
    public int getCount() {
        return mPicks==null?0:mPicks.size();
    }

    @Override
    public Object getItem(int i) {
        return mPicks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (view==null) {
            viewHolder=new ViewHolder();
            view = View.inflate(mContext, R.layout.item_pick, null);
            viewHolder.cb=view.findViewById(R.id.cb_pick);
            viewHolder.tv_name=view.findViewById(R.id.tv_pick_name);
            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)view.getTag();
        }

        viewHolder.tv_name.setText(mPicks.get(i).getUserInfo().getName());
        viewHolder.cb.setChecked(mPicks.get(i).isChecked());
        //判断
        if (mExistMembers.contains(mPicks.get(i).getUserInfo().getHxid())){
            viewHolder.cb.setChecked(true);
            mPicks.get(i).setChecked(true);
        }

        return view;
    }
    //获取选择的联系人
    public List<String> getPickContacts(){
        List<String> picks=new ArrayList<>();
        for (PickContactInfo pick:mPicks){
            //是否选中
            if (pick.isChecked()){
                picks.add(pick.getUserInfo().getName());
            }
        }
        return picks;
    }

    public class ViewHolder{
        private CheckBox cb;
        private TextView tv_name;
    }
}
