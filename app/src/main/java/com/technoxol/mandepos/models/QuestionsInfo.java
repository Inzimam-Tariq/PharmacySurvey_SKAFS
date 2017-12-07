package com.technoxol.mandepos.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jawad Zulqarnain on 9/20/2017.
 */

public class QuestionsInfo implements Serializable {

    private String question_id; //": "2",
    private String template_id; //": "0",
    private String question_text; //": "Town",
    private String question_type; //": "editText",
    private String answer; //": "1",
    private String options_size; //": "1",
    private List<OptionsInfo> options; //": []

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<OptionsInfo> getOptions() {
        return options;
    }

    public void setOptions(List<OptionsInfo> options) {
        this.options = options;
    }

    public String getOptions_size() {
        return options_size;
    }

    public void setOptions_size(String options_size) {
        this.options_size = options_size;
    }
}
