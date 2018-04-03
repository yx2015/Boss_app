package com.xyl.boss_app.bean.params;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahuhxl on 2015/11/5.
 */
public abstract class BaseParams {
    protected Map<String, String> params = new HashMap<>();
    public abstract Map<String, String> getMapParams();
}
