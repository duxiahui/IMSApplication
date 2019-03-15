package com.example.imsapplication.modle.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.imsapplication.modle.dao.ContactTable;
import com.example.imsapplication.modle.dao.InviteTable;


/**
 * Created by 吕文杰 on 2018/4/16.
 */

//创建表的帮助类
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建联系人表
        db.execSQL(ContactTable.CREARE_TAB);
        //创建群组表
        db.execSQL(InviteTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
