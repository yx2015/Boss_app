package com.xyl.boss_app.bean.params;

import android.util.Log;

import com.xyl.boss_app.utils.GsonUtils;

import java.util.Map;

/**
 * Created by yx on 2015/12/23 0023.
 */
public class FeeParams extends BaseParams {
    private String feeType;//费用类型，应收为1，应付为2，利润为空
    private String timeType;//时间类型，送货时间为1，业务时间为2，装卸时间为3
    private int pageSize;//分页大小，默认4
    private int pageNo;//当前页面，默认1
    private String startTime;//开始时间，值如2015-11-11
    private String endTime;// 结束时间，值如2015-11-11

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public int getPageSize() {
        return pageSize == 0 ? 5 : pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo == 0 ? 1 : pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public Map<String, String> getMapParams() {
        params.put("feeType", feeType);
        params.put("timeType", timeType);
        params.put("pageSize", getPageSize() + "");
        params.put("pageNo", getPageNo() + "");
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        Log.d("业务费用请求参数", GsonUtils.toJson(params));
        return params;
    }
}
