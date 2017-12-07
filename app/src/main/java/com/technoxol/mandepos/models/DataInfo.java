package com.technoxol.mandepos.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jawad Zulqarnain on 9/20/2017.
 */

public class DataInfo implements Serializable {

    private String category_id; //": "1",
    private String category_title; //": "Medical Store",
    private String category_description; //": "Pharmacy without Dispensing/Medical Store",
    private String category_image; //": "http://www.skafs.com/survey/image/catalog/store.png",

    public List<QuestionsInfo> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionsInfo> questions) {
        this.questions = questions;
    }

    private List<QuestionsInfo> questions;

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public void setCategory_description(String category_description) {
        this.category_description = category_description;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getCategory_title() {
        return category_title;
    }

    public String getCategory_description() {
        return category_description;
    }

    public String getCategory_image() {
        return category_image;
    }
}
