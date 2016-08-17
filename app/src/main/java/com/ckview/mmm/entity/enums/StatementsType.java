/**
 * 项目名称：ManageMoneyMatters <br/>
 * 文件名称: StatementsType.java <br/>
 * Created by 谌珂 on 2016/8/16.  <br/>
 */
package com.ckview.mmm.entity.enums;

import android.support.annotation.StringRes;

import com.ckview.mmm.R;

/**
 * 项目名称：ManageMoneyMatters <br/>
 * 类  名: StatementsType <br/>
 * 类描述: <br/>
 * 版    本：1.0.0 <br/>
 * 修改时间：2016/8/16 <br/>
 * @author 谌珂 <br/>
 */
public enum StatementsType {
    EAT(0, R.string.statements_type_eat);

    private int value = 1;
    private @StringRes int name = 0;

    StatementsType(int value, int name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public int getName() {
        return this.name;
    }

    public static StatementsType setValue(int name) {
        StatementsType[] arr$ = values();
        for (StatementsType c : arr$) {
            if (c.getName() == name) {
                return c;
            }
        }

        return EAT;
    }
}
