package com.xyl.boss_app.bean.params;

import java.util.Map;

/**
 * @ClassName StatisticParams
 * @Description:首页统计数据请求参数
 * Created by yx on 2015/12/18 0018.
 */
public class StatisticParams extends BaseParams {
    private String timeType;//时间类型，送货时间为1，业务时间为2，装卸时间为3
    private String startTime;// 开始时间，值如2015-11-11
    private String endTime;// 结束时间，值如2015-11-11

    public StatisticParams(String timeType, String startTime, String endTime) {
        this.timeType = timeType;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public StatisticParams() {
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
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
        params.put("timeType", timeType);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        return params;
    }
}
