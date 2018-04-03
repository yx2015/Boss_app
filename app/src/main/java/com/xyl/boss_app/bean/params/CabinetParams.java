package com.xyl.boss_app.bean.params;

import java.util.Map;

/**
 * Created by yx on 2015/12/24 0024.
 */
public class CabinetParams extends BaseParams {
    private String bizStatus;
    private int pageSize;
    private int pageNo;
    public CabinetParams(String bizStatus,int pageSize,int pageNo) {
        this.bizStatus=bizStatus;
        this.pageSize=pageSize;
        this.pageNo=pageNo;
    }

    @Override
    public Map<String, String> getMapParams() {
        params.put("bizStatus",bizStatus);
        params.put("pageSize",pageSize+"");
        params.put("pageNo",pageNo+"");
        return params;
    }
}
