package com.technoxol.mandepos.models;

import java.io.Serializable;

/**
 * Created by Jawad Zulqarnain on 9/22/2017.
 */

public class StatesInfo implements Serializable {

    String state_id;
    String stat_name;

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getStat_name() {
        return stat_name;
    }

    public void setStat_name(String stat_name) {
        this.stat_name = stat_name;
    }
}
