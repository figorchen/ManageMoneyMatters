/**
 * 项目名称：ManageMoneyMatters <br/>
 * 文件名称: Statements.java <br/>
 * Created by 谌珂 on 2016/8/16.  <br/>
 */
package com.ckview.mmm.entity.db;

import com.uuzz.android.util.database.annotation.TableProperty;

import java.io.Serializable;

/**
 * 项目名称：ManageMoneyMatters <br/>
 * 类  名: Statements <br/>
 * 类描述: 流水数据的Bean<br/>
 * 版    本：1.0.0 <br/>
 * 修改时间：2016/8/16 <br/>
 * @author 谌珂 <br/>
 */
public class Statements implements Serializable {
    /** 收入的标记，大于0小于1999的都是支出 */
    public static final int OUTCOME = 1999;
    /** 收入的标记，大于1999小于2999的都是收入 */
    public static final int INCOME = 2999;

    @TableProperty
    private int id;
    @TableProperty
    private long sTimestamp;
    @TableProperty
    private int sUserId;
    @TableProperty
    private int sMoneyAccountId;
    @TableProperty
    private int mStatementsType;
    @TableProperty
    private double sMoney;
    @TableProperty
    private String sDescription;
    @TableProperty
    private String sImages;
    @TableProperty
    private int sRelationId;
    @TableProperty
    private int sRelationType;
    @TableProperty
    private int sSpender;

    public int getsSpender() {
        return sSpender;
    }

    public void setsSpender(int sSpender) {
        this.sSpender = sSpender;
    }

    public Statements() {
    }

    public int getId() {
        return id;
    }

    public long getsTimestamp() {
        return sTimestamp;
    }

    public void setsTimestamp(long sTimestamp) {
        this.sTimestamp = sTimestamp;
    }

    public int getsUserId() {
        return sUserId;
    }

    public void setsUserId(int sUserId) {
        this.sUserId = sUserId;
    }

    public int getsMoneyAccountId() {
        return sMoneyAccountId;
    }

    public void setsMoneyAccountId(int sMoneyAccountId) {
        this.sMoneyAccountId = sMoneyAccountId;
    }

    public int getmStatementsType() {
        return mStatementsType;
    }

    public void setmStatementsType(int mStatementsType) {
        this.mStatementsType = mStatementsType;
    }

    public double getsMoney() {
        return sMoney;
    }

    public void setsMoney(double sMoney) {
        this.sMoney = sMoney;
    }

    public String getsDescription() {
        return sDescription;
    }

    public void setsDescription(String sDescription) {
        this.sDescription = sDescription;
    }

    public String getsImages() {
        return sImages;
    }

    public void setsImages(String sImages) {
        this.sImages = sImages;
    }

    public int getsRelationId() {
        return sRelationId;
    }

    public void setsRelationId(int sRelationId) {
        this.sRelationId = sRelationId;
    }

    public int getsRelationType() {
        return sRelationType;
    }

    public void setsRelationType(int sRelationType) {
        this.sRelationType = sRelationType;
    }

    public Statements(int id, long sTimestamp, int sUserId, int sMoneyAccountId, int mStatementsType, double sMoney, String sDescription, String sImages, int sRelationId, int sRelationType, int sSpender) {
        this.id = id;
        this.sTimestamp = sTimestamp;
        this.sUserId = sUserId;
        this.sMoneyAccountId = sMoneyAccountId;
        this.mStatementsType = mStatementsType;
        this.sMoney = sMoney;
        this.sDescription = sDescription;
        this.sImages = sImages;
        this.sRelationId = sRelationId;
        this.sRelationType = sRelationType;
        this.sSpender = sSpender;
    }
}
