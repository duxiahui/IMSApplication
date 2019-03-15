package com.example.imsapplication.modle.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.imsapplication.modle.bean.UserInfo;
import com.example.imsapplication.modle.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 吕文杰 on 2018/4/16.
 */
    //联系人的操作类
public class ContactTabDao {
    private DBHelper mHelper;

    public ContactTabDao(DBHelper mHelper) {
        this.mHelper = mHelper;
    }
    //获取所有联系人
    public List<UserInfo> getContacts(){
        //获取数据库连接
        SQLiteDatabase db=mHelper.getReadableDatabase();
        //执行查找语句
        String sql="select * from "+ContactTable.TAB_NAME+" where "+ContactTable.COL_IS_CONTACT+"=1";
        Cursor cursor=db.rawQuery(sql,null);
        List<UserInfo> users=new ArrayList<UserInfo>();
        UserInfo userInfo=null;
        while (cursor.moveToNext()){
            userInfo =new UserInfo();
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
            users.add(userInfo);
        }
        cursor.close();
        return users;
    }
    //通过环信ID获取联系人的单个信息
    public UserInfo getContactByHx(String hxId){
        if (hxId==null){
            return null;
        }
        //获取数据库连接
        SQLiteDatabase db=mHelper.getReadableDatabase();
        //执行查询语句
        String sql="select * from "+ContactTable.TAB_NAME+" where "+ContactTable.COL_HXID+"=?";
        Cursor cursor=db.rawQuery(sql,new String[]{hxId});
        UserInfo userInfo=null;

        while (cursor.moveToNext()){
            userInfo =new UserInfo();
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));

        }
    cursor.close();

        return userInfo;
    }
    //通过环信ID 获取用户联系人信息
    public List<UserInfo> getContactByHx(List<String> hxIds){
        if (hxIds==null||hxIds.size()<=0){
            return  null;
        }
    List<UserInfo> userInfos=new ArrayList<UserInfo>();
            //遍历ID
        for (String hxid:hxIds){
            UserInfo contact=getContactByHx(hxid);
            userInfos.add(contact);
        }
        return userInfos;
    }
    //保存联系人
    public void savaContact(UserInfo user,boolean isMyContact){
        if (user==null){
            return;
        }
        //获取数据库连接
        SQLiteDatabase db=mHelper.getReadableDatabase();
        //执行保存的语句
        ContentValues values=new ContentValues();
        values.put(ContactTable.COL_HXID,user.getHxid());
        values.put(ContactTable.COL_NAME,user.getName());
        values.put(ContactTable.COL_NICK,user.getNick());
        values.put(ContactTable.COL_PHOTO,user.getPhoto());
        values.put(ContactTable.COL_IS_CONTACT,isMyContact?1:0);
        db.replace(ContactTable.TAB_NAME,null,values);

    }

    //保存好多联系人信息
    public void savaContacts(List<UserInfo> users,boolean isMyContact){
        if (users==null||users.size()<=0){
            return;
        }

        for (UserInfo user:users){
            savaContact(user,isMyContact);
        }

    }

    //删除联系人
    public void deleteContactByHxId(String hxId){
        if (hxId==null){
            return;
        }
        //获取数据库连接
        SQLiteDatabase db=mHelper.getReadableDatabase();
        db.delete(ContactTable.TAB_NAME,ContactTable.COL_HXID+"=?",new String[]{hxId});

    }
}
