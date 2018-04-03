package com.xyl.boss_app.common;

/**
 * @ClassName: UrlConstant
 * @Description: Url信息
 * Created by yx on 2015/12/18 0018.
 */
public class UrlConstant {
    //    public static final String HOST = "http://172.16.88.103:8140";//测试
    public static final String HOST = "http://182.254.157.24:8140";//预发布
    //    public static final String HOST = " http://jylb.56xyl.com:8140";//付攀
    public static final String HOST_FILE = "http://172.16.88.81";//付攀
    private static final String JS = "http://172.16.88.108";//本地服务器
    //    private static final String JS ="http://js.56xyl.com/" ;//生产

    public static final String LOGIN = HOST + "/JYLB/ws/user/login";//用户登录接口
    public static final String GET_APPINFO = HOST + "/JYLB/ws/base/appInfo";//app版本信息接口
    public static final String GET_STATISTICS_FEE = HOST + "/JYLB/ws/business/statisticsFee";//首页业务费用统计数据接口
    public static final String GET_STATISTICS_CABINET = HOST + "/JYLB/ws/business/statisticsCabinet";//首页统计数据接口
    public static final String GET_FEE_LIST = HOST + "/JYLB/ws/business/feeList";//业务费用列表接口
    public static final String GET_FEE_LIST_TOTAL = HOST + "/JYLB/ws/business/feeListTotal";//业务费用列表接口
    public static final String GET_CABINET_LIST = HOST + "/JYLB/ws/business/cabinetList";//业务柜量列表接口
    public static final String GET_CABINET_LIST_TOTAL = HOST + "/JYLB/ws/business/cabinetListTotal";//业务柜量列表接口

    public static final String HOW_TO_REGISTER = JS + "/bossapp/registered.html";//如何注册APP页面
    public static final String FORGET_PWD = JS + "/bossapp/forgetpw.html";//忘记密码页面
}
