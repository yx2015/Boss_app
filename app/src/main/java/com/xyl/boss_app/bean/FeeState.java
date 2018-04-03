package com.xyl.boss_app.bean;

import android.support.v4.app.Fragment;

/**
 * Created by yx on 2015/12/17 0017.
 */
public class FeeState {
    private Fragment state;
    private String name;

    public FeeState(String name, Fragment state) {
        this.state = state;
        this.name = name;
    }

    public Fragment getState() {
        return state;
    }

    public void setState(Fragment state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
