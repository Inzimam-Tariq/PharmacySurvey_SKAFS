package com.technoxol.mandepos.models;

import java.io.Serializable;

/**
 * Created by Jawad Zulqarnain on 9/22/2017.
 */

public class TownsInfo implements Serializable {

    String town_id;
    String name;

    public String getTown_id() {
        return town_id;
    }

    public void setTown_id(String town_id) {
        this.town_id = town_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
