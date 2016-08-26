/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: MoneyTextWatcher.java <br/>
 * <p>
 * Created by 谌珂 on 2016/8/26.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.ckview.mmm.ui.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 项目名称：手机大管家 <br/>
 * 类  名: MoneyTextWatcher <br/>
 * 类描述: <br/>
 * 实现的主要功能 <br/>
 * 版    本：1.0.0 <br/>
 * 修改时间：2016/8/26 <br/>
 * @author 谌珂 <br/>
 */
public class MoneyTextWatcher implements TextWatcher {

    /** 编辑框 */
    private EditText mEditText;

    public MoneyTextWatcher(EditText mEditText) {
        this.mEditText = mEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String value = s.toString();
        String[] split = value.split("\\.");
        if(split.length == 2 && split[1].length() > 2) {
            mEditText.setText(value.substring(0, value.length() - split[1].length() + 2));
            mEditText.setSelection(mEditText.length());
        }
    }
}
