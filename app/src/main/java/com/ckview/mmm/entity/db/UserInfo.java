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
    @TableProperty
    private boolean mPayer;
    @TableProperty
    private boolean mLogin;

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

    public Boolean getmPayer() {
        return mPayer;
    }

    public void setmPayer(Boolean mPayer) {
        this.mPayer = mPayer;
    }

    public Boolean getmLogin() {
        return mLogin;
    }

    public void setmLogin(Boolean mLogin) {
        this.mLogin = mLogin;
    }
}
