package com.technoxol.mandepos.models;

import java.io.Serializable;

/**
 * Created by Jawad Zulqarnain on 9/22/2017.
 */

public class CategoriesInfo implements Serializable {

    String category_id;
    String category_name;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
