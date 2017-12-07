package com.technoxol.mandepos.models;

import java.io.Serializable;

/**
 * Created by Jawad Zulqarnain on 9/20/2017.
 */

public class OptionsInfo implements Serializable {

    private String question_number; // ": "1",
    private String option_id; //": "3",
    private String option_text; //": "Medical Store",
    private String option_value; //": "0"

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public String getOption_text() {
        return option_text;
    }

    public void setOption_text(String option_text) {
        this.option_text = option_text;
    }

    public String getOption_value() {
        return option_value;
    }

    public void setOption_value(String option_value) {
        this.option_value = option_value;
    }

    public String getQuestion_number() {
        return question_number;
    }

    public void setQuestion_number(String question_number) {
        this.question_number = question_number;
    }
}
