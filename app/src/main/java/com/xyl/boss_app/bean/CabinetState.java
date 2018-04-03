package com.xyl.boss_app.bean;

/**
 * Created by ahuhxl on 2015/11/10.
 *
 * @Description: TODO (描述这个类的作用)
 */
public class CabinetState {
    private String state;
    private String name;

    public CabinetState(String name, String state) {
        this.state = state;
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}