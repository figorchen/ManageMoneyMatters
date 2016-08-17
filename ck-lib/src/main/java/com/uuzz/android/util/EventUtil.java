package com.uuzz.android.util;

import android.view.MotionEvent;
import android.view.View;

public class EventUtil {

    private static EventUtil mInstance;

    private EventUtil() {}

    public synchronized static EventUtil getInstance() {
        if (mInstance == null) {
            mInstance = new EventUtil();
        }
        return mInstance;
    }

    private static final long DEFAULT_DOUBLE_DELAY = 500;

    /**
     * 描 述：给View设置双击事件监听<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/6/20 注释 <br/>
     * @param v 需要设置双击事件的View
     * @param listener 双击回调
     * @param delay 双击时间间隔
     */
    public void setOnDoubleClickListener(View v, OnDoubleClickListener listener, long delay) {
        DoubleCLickListener lDoubleCLickListener = new DoubleCLickListener(delay, listener);
        lDoubleCLickListener.setOnDoubleClickListener(v);
    }

    /**
     * 描 述：给View设置双击事件监听，默认双击间隔500ms<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/6/20 注释 <br/>
     * @param v 需要设置双击事件的View
     * @param listener 双击回调
     */
    public void setOnDoubleClickListener(View v, OnDoubleClickListener listener) {
        setOnDoubleClickListener(v, listener, DEFAULT_DOUBLE_DELAY);
    }

    public interface OnDoubleClickListener {
        void onDoubleClick(View v);
    }

    private class DoubleCLickListener implements View.OnTouchListener {
        public DoubleCLickListener(long delay, OnDoubleClickListener listener) {
            this.delay = delay;
            this.listener = listener;
        }

        /** 计算点击的次数 */
        private int count = 0;
        /** 第一次点击的时间 long型 */
        private long firstClick = 0;
        /** 最后一次点击的时间 */
        private long lastClick = 0;
        /** 默认的双击事件间隔 */
        private long delay = 500;
        /** 双击回调事件 */
        private OnDoubleClickListener listener;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    // 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
                    if (firstClick != 0 && System.currentTimeMillis() - firstClick > delay) {
                        clear();
                    }
                    if (++count == 1) {
                        firstClick = System.currentTimeMillis();
                    }
                    else if (count == 2) {
                        lastClick = System.currentTimeMillis();
                        // 两次点击小于500ms 也就是连续点击
                        if (lastClick - firstClick < delay) {
                            clear();
                            listener.onDoubleClick(v);
                        }
                    }
                    break;
            }
            return false;
        }

            // 清空状态
        private void clear() {
            count = 0;
            firstClick = 0;
            lastClick = 0;
        }

        /**
         * 描 述：给View设置双击监听<br/>
         * 作 者：谌珂<br/>
         * 历 史: (版本) 谌珂 2016/6/20 注释 <br/>
         */
        public void setOnDoubleClickListener(View v) {
            v.setOnTouchListener(this);
        }
    }
}
