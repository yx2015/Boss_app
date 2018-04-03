package com.xyl.boss_app.bean.params;

import java.util.Map;

/**
 * @ClassName LoginParams
 * @Description:登录请求参数 Created by yx on 2015/12/18 0018.
 */
public class LoginParams extends BaseParams {
    private String orgCode;//公司代码
    private String username;//用户名
    private String password;//密码
    private String source;//密码

    public LoginParams(String orgCode, String username, String password,String source) {
        this.orgCode = orgCode;
        this.username = username;
        this.password = password;
        this.source=source;
    }

    @Override
    public Map<String, String> getMapParams() {
        params.put("orgCode", orgCode);
        params.put("username", username);
        params.put("password", password);
        params.put("source", source);
        return params;
    }
}
