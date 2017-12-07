package com.technoxol.mandepos.models;

import java.io.Serializable;

/**
 * Created by Jawad Zulqarnain on 9/22/2017.
 */

public class CitiesInfo implements Serializable {

    String city_id;
    String name;

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
