package com.example.imsapplication.modle.db;

import android.content.Context;
import com.example.imsapplication.modle.dao.ContactTabDao;
import com.example.imsapplication.modle.dao.InviteTableDao;

/**
 * Created by 吕文杰 on 2018/4/16.
 */

public class DBManager {

    private DBHelper  dbHelper;
    private ContactTabDao contactTabDao;
    private InviteTableDao inviteTableDao;
    public DBManager(Context context,String name){
        //创建数据库
        dbHelper=new DBHelper(context,name);
        //创建数据库中的表的操作类
        contactTabDao=new ContactTabDao(dbHelper);
        inviteTableDao=new InviteTableDao(dbHelper);

    }
    //获取联系人操作类对象
    public ContactTabDao getContactTabDao(){
        return contactTabDao;
    }
    //获取邀请操作类对象
    public InviteTableDao getinviteTableDao(){
        return inviteTableDao;
    }
    //关闭数据库
    public void close(){
        dbHelper.close();
    }

}
