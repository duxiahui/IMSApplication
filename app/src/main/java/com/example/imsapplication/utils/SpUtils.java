package com.example.imsapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.imsapplication.IMApplication;


/**
 * Created by 吕文杰 on 2018/4/17.
 */
//保存和获取数据
public class SpUtils {
    public static final String IS_NEW_INVITE="is_new_invite";//新的邀请标记
    private static SpUtils spUtils=new SpUtils();

    private static SharedPreferences mSp;

    private  SpUtils(){}
    public static  SpUtils getInstance(){
        if (mSp==null) {
            mSp = IMApplication.getGlobalApplication().getSharedPreferences("im", Context.MODE_PRIVATE);
        }
        return spUtils;
    }
    //保存

    public void save(String key,Object value){
        if (value instanceof String){
            mSp.edit().putString(key,(String)value).commit();
        }else if (value instanceof Boolean){
            mSp.edit().putBoolean(key,(Boolean)value).commit();
        }else if (value instanceof Integer){
            mSp.edit().putInt(key,(int)value).commit();
        }
    }


    //获取数据的方法
    public String getString(String key,String defValue){
        return  mSp.getString(key,defValue);
    }
    //获取boolean
    public boolean getBoolean(String key,Boolean defValue){
        return  mSp.getBoolean(key,defValue);
    }
    //获取int 类型的数据
    public int getInt(String key,int defValue){
        return  mSp.getInt(key,defValue);
    }

}
