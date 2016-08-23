package com.ckview.mmm.entity.db;

import com.uuzz.android.util.database.annotation.TableProperty;

public class StatementType {
    @TableProperty
    private int typeId;
    @TableProperty
    private String sName;
    @TableProperty
    private double sThreshold;
    @TableProperty
    private int sParentId;
    @TableProperty
    private int sLeftBound;
    @TableProperty
    private int sRightBound;
    @TableProperty
    private double sIsLeaf;

    public int getId() {
        return typeId;
    }

    public void setId(int id) {
        this.typeId = id;
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

    public double getsIsLeaf() {
        return sIsLeaf;
    }

    public void setsIsLeaf(double sIsLeaf) {
        this.sIsLeaf = sIsLeaf;
    }
}
