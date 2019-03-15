package com.example.imsapplication.modle.dao;

/**
 * Created by 吕文杰 on 2018/4/11.
 */

public class UserAccountTable {
    public static final  String TAB_NAME="tab_name";
    public static final String COL_NAME="name";
    public static final String COL_HXID="hxid";
    public static final String COL_NICK="nick";
    public static final String COL_PHOTO="photo";

    public static final String CREATE_TAB="create table "+TAB_NAME+" ("+COL_HXID+" text primary key,"+COL_NAME+" text,"+COL_NICK+" text,"+COL_PHOTO+" text);";
}
