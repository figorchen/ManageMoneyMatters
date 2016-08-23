/**
 * 项目名称：ManageMoneyMatters <br/>
 * 文件名称: Statements.java <br/>
 * Created by 谌珂 on 2016/8/16.  <br/>
 */
package com.ckview.mmm.entity.db;

import com.uuzz.android.util.TimeUtil;
import com.uuzz.android.util.database.annotation.TableProperty;
import com.uuzz.android.util.database.annotation.TablePropertyExtra;

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
    /** 还款支出的标记 */
    public static final int OUTCOME_REPAYMENT = 1002;
    /** 信用卡还款支出标记 */
    public static final int OUTCOME_CREDIT_CARD_PAYMENT = 1003;
    /** 还款收入标记 */
    public static final int INCOME_REPAYMENT = 2002;

    @TableProperty
    private int id;
    @TableProperty
    private long sTimestamp = System.currentTimeMillis();
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

    @TableProperty
    @TablePropertyExtra("s_type_name")
    private String sName;
    @TableProperty
    @TablePropertyExtra("s_threshold")
    private double sThreshold;
    @TableProperty
    @TablePropertyExtra("s_parent_id")
    private int sParentId;
    @TableProperty
    @TablePropertyExtra("s_left_bound")
    private int sLeftBound;
    @TableProperty
    @TablePropertyExtra("s_right_bound")
    private int sRightBound;
    @TableProperty
    @TablePropertyExtra("s_leaf")
    private double sIsLeaf;
    /** 流水数据是否改变过 */
    private boolean isChanged;
    /** 流水数据是否已被选中 */
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public double getsIsLeaf() {
        return sIsLeaf;
    }

    public void setsIsLeaf(double sIsLeaf) {
        this.sIsLeaf = sIsLeaf;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public double getsThreshold() {
        return sThreshold;
    }

    public void setsThreshold(double sThreshold) {
        this.sThreshold = sThreshold;
    }

    public int getsParentId() {
        return sParentId;
    }

    public void setsParentId(int sParentId) {
        this.sParentId = sParentId;
    }

    public int getsLeftBound() {
        return sLeftBound;
    }

    public void setsLeftBound(int sLeftBound) {
        this.sLeftBound = sLeftBound;
    }

    public int getsRightBound() {
        return sRightBound;
    }

    public void setsRightBound(int sRightBound) {
        this.sRightBound = sRightBound;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public boolean isChanged() {

        return isChanged;
    }

    public int getsSpender() {
        return sSpender;
    }

    public void setsSpender(int sSpender) {
        isChanged = true;
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
        isChanged = true;
        this.sUserId = sUserId;
    }

    public int getsMoneyAccountId() {
        return sMoneyAccountId;
    }

    public void setsMoneyAccountId(int sMoneyAccountId) {
        isChanged = true;
        this.sMoneyAccountId = sMoneyAccountId;
    }

    public int getmStatementsType() {
        return mStatementsType;
    }

    public void setmStatementsType(int mStatementsType) {
        isChanged = true;
        this.mStatementsType = mStatementsType;
    }

    public double getsMoney() {
        return sMoney;
    }

    public void setsMoney(double sMoney) {
        isChanged = true;
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

    /**
     * 描 述：返回流水数据对应的日期<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/23 <br/>
     */
    public String getTime() {
        return TimeUtil.getTime(sTimestamp, TimeUtil.DEFAULT_FORMAT_YYYYMMDD);
    }
}
