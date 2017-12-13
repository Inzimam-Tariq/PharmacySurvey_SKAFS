package com.technoxol.mandepos;


import com.technoxol.mandepos.models.SubCatInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ArdSoft on 4/11/2017.
 */

public class AppConstants {

    public static final String ALREADY_LOGIN = "alreadyLogin";

    public static final String PHARMACIST_ID = "pharmacist_id";
    public static final String PHARMACIST_IMEI = "imei";
    public static final String PHARMACIST_EMAIL = "email";
    public static final String PHARMACIST_PASSWORD = "password";
    public static final String PHARMACIST_ACCESS = "access";
    public static final String REG_STATUS = "regStatus";

    public static final String SURVEY_ID = "surveyId";
    public static final String SUB_CAT_ID = "subCatId";
    public static final String SUB_CAT_TYPE = "subCatType";

    public static final String STATES_LIST = "statesList";
    public static final String CATEGORY_LIST = "categoryList";

    public static final String LICENSE_RESPONSE = "licenseResponse";
    public static final String LICENSE_NUMBER = "licenseNumber";
    public static List<SubCatInfo> subCatInfoList = new ArrayList<>();

    public static int totalLength = 0;

    public static List<String> selected = new ArrayList<>();
    public static List<String> answers_id = new ArrayList<>();
    public static List<String> answers = new ArrayList<>();
    public static List<String> answersText = new ArrayList<>();
    public static List<String> answersPosition = new ArrayList<>();

    public static final String NOTIF_TITLE = "notifTitle";
    public static final String NOTIF_MSG = "notifMsg";
    private static String Survey_Id;

    public static String getSurveyId() {
        return Survey_Id;
    }

    public static void setSurveyId(String surveyId) {
        Survey_Id = surveyId;
    }
}


