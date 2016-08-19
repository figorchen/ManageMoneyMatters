package com.ckview.mmm.entity.db;

import com.uuzz.android.util.database.annotation.TableProperty;
import com.uuzz.android.util.database.annotation.TablePropertyExtra;

public class MoneyAccount {
    @TableProperty
    private int id;
    @TableProperty
    @TablePropertyExtra("c_card_id")
    private int cCardId;
    @TableProperty
    @TablePropertyExtra("m_money_account_id")
    private int mTypeId;
    @TableProperty
    private int mUserId;
    @TableProperty
    private int mAccountType;
    @TableProperty
    private int mCardId;
    @TableProperty
    private boolean mDisposable;
    @TableProperty
    private String mDescription;
    @TableProperty
    @TablePropertyExtra("m_type_name")
    private String mTypeName;
    @TableProperty
    @TablePropertyExtra("c_card_number")
    private String cCardNumber;
    @TableProperty
    @TablePropertyExtra("c_bank_name")
    private String cBankName;
    @TableProperty
    private double mBalance;
    @TableProperty
    private int mUseCount;
    @TableProperty
    private long mStatementDay;
    @TableProperty
    private long mRepaymentDay;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getcCardId() {
        return cCardId;
    }

    public void setcCardId(int cCardId) {
        this.cCardId = cCardId;
    }

    public int getmTypeId() {
        return mTypeId;
    }

    public void setmTypeId(int mTypeId) {
        this.mTypeId = mTypeId;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public int getmAccountType() {
        return mAccountType;
    }

    public void setmAccountType(int mAccountType) {
        this.mAccountType = mAccountType;
    }

    public int getmCardId() {
        return mCardId;
    }

    public void setmCardId(int mCardId) {
        this.mCardId = mCardId;
    }

    public boolean ismDisposable() {
        return mDisposable;
    }

    public void setmDisposable(boolean mDisposable) {
        this.mDisposable = mDisposable;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmTypeName() {
        return mTypeName;
    }

    public void setmTypeName(String mTypeName) {
        this.mTypeName = mTypeName;
    }

    public String getcCardNumber() {
        return cCardNumber;
    }

    public void setcCardNumber(String cCardNumber) {
        this.cCardNumber = cCardNumber;
    }

    public String getcBankName() {
        return cBankName;
    }

    public void setcBankName(String cBankName) {
        this.cBankName = cBankName;
    }

    public double getmBalance() {
        return mBalance;
    }

    public void setmBalance(double mBalance) {
        this.mBalance = mBalance;
    }

    public int getmUseCount() {
        return mUseCount;
    }

    public void setmUseCount(int mUseCount) {
        this.mUseCount = mUseCount;
    }

    public long getmStatementDay() {
        return mStatementDay;
    }

    public void setmStatementDay(long mStatementDay) {
        this.mStatementDay = mStatementDay;
    }

    public long getmRepaymentDay() {
        return mRepaymentDay;
    }

    public void setmRepaymentDay(long mRepaymentDay) {
        this.mRepaymentDay = mRepaymentDay;
    }
}
