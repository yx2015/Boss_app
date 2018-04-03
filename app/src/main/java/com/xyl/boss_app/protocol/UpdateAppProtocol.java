package com.xyl.boss_app.protocol;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hxl.universallibrary.utils.VolleyHelper;
import com.xyl.boss_app.bean.UpdateInfoDto;
import com.xyl.boss_app.common.UrlConstant;
import com.xyl.boss_app.utils.UIUtils;

/**
 * @ClassName UpdateAPPProtocol
 * @Decription: 登录网络协议
 * Created by yx on 2015/12/18 0018.
 */
public class UpdateAppProtocol {
    public UpdateAppProtocol() {
        NetWorkRequest request = new NetWorkRequest(UrlConstant.GET_APPINFO, UpdateInfoDto.class, new Response.Listener<UpdateInfoDto>() {
            @Override
            public void onResponse(UpdateInfoDto updateInfoDto) {
                if ("200".equals(updateInfoDto.getCode())) {
                    UpdateInfoDto.UpdateAppInfo updateInfo = updateInfoDto.getData();
                    onCallBack(updateInfo);
                } else {
                    UIUtils.showToast(updateInfoDto.getMsg());

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                UIUtils.showToast(volleyError.getMessage());
                errorBack();
            }
        });
        VolleyHelper.getInstance().getRequestQueue().add(request);
    }

    protected void onCallBack(UpdateInfoDto.UpdateAppInfo updateInfo) {

    }

    protected void errorBack(){}


}
