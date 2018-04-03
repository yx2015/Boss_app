package com.xyl.boss_app.common;

/**
 * Created by yx on 2015/12/14 0014.
 */
public class Constants {
    public static final String CUSTOM_PHONE = "075583517399";//客服电话
    public static final String ACCOUNTID = "accountId";
    public static final String TLOG_TAG = "tlogTag";
    public static final String SOURCE = "0";//登录来源：0为Android,1为IOS

    public static final int EVENT_BEGIN = 0x100;
    public static final int EVENT_YINGSHOU = EVENT_BEGIN + 1;
    public static final int EVENT_YINGFU = EVENT_BEGIN + 2;
    public static final int EVENT_PROFITS = EVENT_BEGIN + 3;
    public static final int EVENT_START_BIZ = EVENT_BEGIN + 4;
    public static final int EVENT_ON_BIZ = EVENT_BEGIN + 5;
    public static final int EVENT_END_BIZ = EVENT_BEGIN + 6;
    public static final int EVENT_AUTHENTICATION_FAILURE = EVENT_BEGIN + 7;


    public class Extra {
        public static final String WEBVIEW_URL = "webview_url";
        public static final String WEBVIEW_TITLE = "webview_title";
    }

    public class Preferences {
        public static final String LAST_LOGIN_ORG_NAME = "last_login_org_name";
        public static final String LAST_LOGIN_ORG_ACCOUNT = "last_login_org_account";
        public static final String LAST_LOGIN_USER_ACCOUNT = "last_login_user_account";
        public static final String LAST_LOGIN_ORG_LOGO = "last_login_org_logo";
        public static final String ORG_NUMS = "org_nums";
        public static final String USER_NUMS = "user_nums";
        public static final String ORG_LOGOS = "org_logos";
        public static final String VERSION_NAME = "verson_name";
        public static final String PWD = "password";
        public static final String NAME = "name";
        public static final String LAST_LOGIN_COMPANY_ID = "companyId";
    }

    /**
     * @ClassName: Database
     * @Description:数据库配置信息
     * @author: Haoxl
     * @date: 2015-3-27 上午9:22:45
     */
    public static final class Database {
        /**
         * 数据库的版本号.
         */
        public static final String DB_NAME = "org";

        /**
         * 数据库的版本号.
         */
        public static final int VERSION = 1;

        /**
         * 数据库的扩展名.
         */
        public static final String EXTENSION = ".db";

    }
}
