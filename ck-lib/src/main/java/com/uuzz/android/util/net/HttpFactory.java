/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: HttpFactory.java <br/>
 * <p/>
 * Created by 谌珂 on 2015/12/31.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.uuzz.android.util.net;

import com.uuzz.android.util.net.http.BaseHttp;
import com.uuzz.android.util.net.http.DownloadHttp;
import com.uuzz.android.util.net.http.TextHttp;

/**
 * 项目名称：手机大管家 <br/>
 * 类  名: HttpFactory <br/>
 * 类描述: <br/>
 * 实现的主要功能 <br/>
 * 版    本：1.0.0 <br/>
 * 修改时间：2015/12/31 <br/>
 * @author 谌珂 <br/>
 */
public class HttpFactory {

    public static final int TEXT_DATA = 0;
    public static final int DOWNLOAD_DATA = 1;

    /**
     * 描 述：获取网络请求的工具对象<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/31 注释 <br/>
     * @param type 需要的对象类型
     * @return BaseHttp类型的对象
     */
    public static BaseHttp getHttpHelper(int type) {
        BaseHttp result;
        switch (type){
            case TEXT_DATA:
                result = new TextHttp();
                break;
            case DOWNLOAD_DATA:
                result = new DownloadHttp();
                break;
            default:
                result = new TextHttp();
                break;
        }
        return result;
    }
}
