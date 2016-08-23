package com.ckview.mmm.db;

public class Common {
    /** 监听者标记，获取可支配资金账户 */
    public static final int DISPOSABLE_ACCOUNT = 0;
    /** 获取借款收入的流水 */
    public static final int INCOME_BORROW_STATEMENTS = 1;
    /** 监听者标记 通过流水类型获取流水意图 */
    public static final int GET_INTENT_FROM_STATEMENT = 2;
    /** 用于监听者发送通知时的标记，全部用户 */
    public static final int ALL_USER_INFO = 3;
    /** 用于监听者发送通知时的标记，可支付用户 */
    public static final int PAYABLE_USER_INFO = 4;
}
