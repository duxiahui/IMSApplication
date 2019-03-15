package com.example.imsapplication.modle.bean;

/**
 * Created by 吕文杰 on 2018/4/27.
 */

public class PickContactInfo {
    private UserInfo userInfo;//联系人
    private boolean isChecked;//是否标记为联系人

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "PickContactInfo{" +
                "userInfo=" + userInfo +
                ", isChecked=" + isChecked +
                '}';
    }

    public PickContactInfo(UserInfo userInfo, boolean isChecked) {
        this.userInfo = userInfo;
        this.isChecked = isChecked;
    }
}
