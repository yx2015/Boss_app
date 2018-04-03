package com.xyl.boss_app.protocol;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.hxl.universallibrary.utils.VolleyHelper;
import com.xyl.boss_app.bean.LoginDto;
import com.xyl.boss_app.bean.params.LoginParams;
import com.xyl.boss_app.common.Constants;
import com.xyl.boss_app.common.UrlConstant;
import com.xyl.boss_app.dao.OrgDao;
import com.xyl.boss_app.db.model.OrgModel;
import com.xyl.boss_app.ui.activity.base.BaseActivity;
import com.xyl.boss_app.utils.SPUtil;
import com.xyl.boss_app.utils.StringUtils;
import com.xyl.boss_app.utils.UIUtils;
import com.xyl.boss_app.widgets.ProgressDialog;

/**
 * @ClassName LoginProtocol
 * @Decription: 登录网络协议
 * Created by yx on 2015/12/18 0018.
 */
public class LoginProtocol {
    private String orgCode, orgLogo, userName, SPuserName;
    private LoginDto.LoginInfo loginInfo;
    private ProgressDialog mProgressDialog;
    private OrgDao orgDao;
    private OrgModel model;

    public LoginProtocol(final LoginParams params) {
        mProgressDialog = new ProgressDialog(BaseActivity.getForegroundActivity());
        mProgressDialog.init();
        mProgressDialog.show();

        NetWorkRequest request = new NetWorkRequest(UrlConstant.LOGIN, params, LoginDto.class, new Response.Listener<LoginDto>() {
            @Override
            public void onResponse(LoginDto loginDto) {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if ("200".equals(loginDto.getCode())) {
                    SPUtil.putBoolean("isLogin", true);
                    loginInfo = loginDto.getData();
                    orgCode = loginInfo.getCode();
                    orgLogo = loginInfo.getLogo();
                    SPUtil.putString(Constants.ACCOUNTID, loginInfo.getAccountId());
                    SPUtil.putString(Constants.Preferences.LAST_LOGIN_COMPANY_ID, loginInfo.getCompanyId());
                    SPUtil.putString(Constants.ACCOUNTID + Constants.Preferences.PWD, loginInfo.getPassword());
                    SPUtil.putString(Constants.ACCOUNTID + Constants.Preferences.NAME, loginInfo.getName());

                    //登陆成功，将公司信息保存到数据库
                    orgDao = new OrgDao();
                    model = new OrgModel();

                    model.setOrgCode(orgCode.toUpperCase());
                    model.setLogo(loginInfo.getLogo());
                    orgDao.save(model);

                    /*保存最近登录用户账号*/
                    SPuserName = SPUtil.getString(Constants.Preferences.USER_NUMS, "");
                    userName = loginInfo.getLoginId();
                    if (StringUtils.isEmpty(SPuserName)) {
                        SPUtil.putString(Constants.Preferences.USER_NUMS, userName + "#" + SPuserName);
                    } else {
                        if (!SPuserName.contains(userName + "#")) {
                            //记录用户账号
                            SPUtil.putString(Constants.Preferences.USER_NUMS, userName + "#" + SPuserName);
                        }
                    }

                    /*保存最后一次登录公司代码、用户名、公司logo以及公司名字*/
                    SPUtil.putString(Constants.Preferences.LAST_LOGIN_ORG_ACCOUNT, orgCode);
                    SPUtil.putString(Constants.Preferences.LAST_LOGIN_USER_ACCOUNT, userName);
                    SPUtil.putString(Constants.Preferences.LAST_LOGIN_ORG_NAME, loginInfo.getCompanyName());
                    SPUtil.putString(Constants.Preferences.LAST_LOGIN_ORG_LOGO, loginInfo.getLogo());
                    onCallBack();
                } else {
                    SPUtil.putBoolean("isLogin", false);
                    UIUtils.showToast(loginDto.getMsg());
                    onError();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (volleyError instanceof TimeoutError) {
                    UIUtils.showToast("连接超时，请检查网络连接~！");
                } else if ((volleyError instanceof NoConnectionError)
                        || (volleyError instanceof NetworkError)) {
                    UIUtils.showToast("网络异常,请稍后再试~！");
                }
            }
        });
        VolleyHelper.getInstance().getRequestQueue().add(request);
    }

    protected void onCallBack() {
    }

    protected void onError() {
    }
}
