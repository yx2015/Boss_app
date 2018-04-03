package com.xyl.boss_app.protocol;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.GsonRequest;
import com.google.gson.reflect.TypeToken;
import com.hxl.universallibrary.eventbus.EventCenter;
import com.hxl.universallibrary.utils.TLog;
import com.xyl.boss_app.bean.BaseDto;
import com.xyl.boss_app.bean.params.BaseParams;
import com.xyl.boss_app.common.Constants;
import com.xyl.boss_app.common.UrlConstant;
import com.xyl.boss_app.ui.activity.base.BaseActivity;
import com.xyl.boss_app.utils.GsonUtils;
import com.xyl.boss_app.utils.PackageUtils;
import com.xyl.boss_app.utils.SPUtil;
import com.xyl.boss_app.utils.SignUtils;
import com.xyl.boss_app.utils.TimeUtils;
import com.xyl.boss_app.utils.UIUtils;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by ahuhxl on 2015/11/5.
 */
public class NetWorkRequest<T> extends GsonRequest {
    private BaseParams mParams;
    private String mUrl;

    public NetWorkRequest(String url, BaseParams params, Response.Listener listener, Response.ErrorListener errorListener) {
        super(url, null, new TypeToken<T>() {
        }.getType(), listener, errorListener);
        this.mParams = params;
        this.mUrl = url;
        this.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,//默认超时时间
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
    }

    public NetWorkRequest(String url, BaseParams params, Class clz, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, null, clz, listener, errorListener);
        this.mParams = params;
        this.mUrl = url;
        this.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,//默认超时时间
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
    }

    public NetWorkRequest(String url, Class clz, Response.Listener listener, Response.ErrorListener errorListener) {
        super(url, null, clz, listener, errorListener);
        this.mUrl = url;
        this.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,//默认超时时间
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        String userId = SPUtil.getString(Constants.ACCOUNTID, "");
        String time = TimeUtils.getCurrentTime();
        String temp = mUrl.contains(UrlConstant.HOST) ? UrlConstant.HOST : UrlConstant.HOST_FILE;
        String sign = SignUtils.getSign(mUrl.substring(temp.length(), mUrl.length()), time);
        String versionName = PackageUtils.getVersionName();

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("u", userId);
        headers.put("t", time);
        headers.put("s", sign);
        headers.put("v", versionName);
        TLog.d("=======", "u=" + userId + ", t=" + time + ", s=" + sign + ", v=" + versionName);
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams == null ? null : mParams.getMapParams();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return super.getBody();
    }

    @Override
    protected VolleyError parseNetworkError(final VolleyError volleyError) {
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            TLog.d("error", new String(volleyError.networkResponse.data));
            String errorJson = new String(volleyError.networkResponse.data);
            BaseDto baseDto = GsonUtils.changeJsonToBean(errorJson, BaseDto.class);
            if ("500".equals(baseDto.getCode())) {
                EventBus.getDefault().post(new EventCenter<>(Constants.EVENT_AUTHENTICATION_FAILURE));
            }
        }
        TLog.d("CurrentActivity", BaseActivity.getCurrentActivity() == null ? "null" : BaseActivity.getCurrentActivity().getClass().getSimpleName());
        if (BaseActivity.getCurrentActivity() != null && !"MainActivity".equals(BaseActivity.getCurrentActivity().getClass().getSimpleName())) {
            BaseActivity.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (volleyError instanceof TimeoutError) {
                        UIUtils.showToast("连接超时，请检查网络连接~！");
                    } else if (volleyError instanceof NoConnectionError) {
                        UIUtils.showToast("连接服务器失败~！");
                    } else if (volleyError instanceof NetworkError) {
                        UIUtils.showToast("网络异常,请稍后再试~！");
                    }
                }
            });
        }
        return volleyError;
    }
}
