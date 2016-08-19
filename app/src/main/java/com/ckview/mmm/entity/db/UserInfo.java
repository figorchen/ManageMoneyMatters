package com.ckview.mmm.entity.db;

import com.uuzz.android.util.database.annotation.TableProperty;

public class UserInfo {
    @TableProperty
    private int id;
    @TableProperty
    private String mUserName;
    @TableProperty
    private String mUserPsd;
    @TableProperty
    private String mUserIcon;

    public UserInfo() {
    }

    public UserInfo(int id, String mUserName, String mUserPsd, String mUserIcon) {

        this.id = id;
        this.mUserName = mUserName;
        this.mUserPsd = mUserPsd;
        this.mUserIcon = mUserIcon;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUserPsd() {
        return mUserPsd;
    }

    public void setmUserPsd(String mUserPsd) {
        this.mUserPsd = mUserPsd;
    }

    public String getmUserIcon() {
        return mUserIcon;
    }

    public void setmUserIcon(String mUserIcon) {
        this.mUserIcon = mUserIcon;
    }
}
