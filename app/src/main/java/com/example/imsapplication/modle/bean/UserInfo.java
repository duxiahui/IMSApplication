package com.example.imsapplication.modle.bean;

/**
 * Created by 吕文杰 on 2018/4/11.
 */
//用户账号信息的bean类
public class UserInfo {
    private String name;//用户名称
    private String hxid;//环信的id
    private String nick;//用户昵称
    private String photo;//用户头像

    public UserInfo() {

    }
    public UserInfo(String name) {
        this.name=name;
    }
    public UserInfo(String name, String hxid, String nick, String photo) {
        this.name = name;
        this.hxid = hxid;
        this.nick = nick;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getHxid() {
        return hxid;
    }

    public String getNick() {
        return nick;
    }

    public String getPhoto() {
        return photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHxid(String hxid) {
        this.hxid = hxid;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", hxid='" + hxid + '\'' +
                ", nick='" + nick + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
