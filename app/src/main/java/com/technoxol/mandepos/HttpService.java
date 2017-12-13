package com.technoxol.mandepos;

import android.content.Context;

import java.util.HashMap;

import static com.technoxol.mandepos.AppConstants.SURVEY_ID;
import static com.technoxol.mandepos.AppConstants.getSurveyId;

public class HttpService extends HttpUtils {

    public static final String BASE_URL = "https://www.farmacy.live/";
    private String authToken = null;
    private String deviceType = "android";
    private String deviceToken = null;
    private SharedPrefUtils sharedPrefUtils;

    public HttpService(Context mContext) {
        super(mContext);
        sharedPrefUtils = new SharedPrefUtils(mContext);
//        authToken = sharedPrefUtils.getSharedPrefValue(AppConstants.SP_APP_TOKEN);
    }

    public void getFormData(String id, HttpResponseCallback callback) {

        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put("tag", "getquestion");
        httpParams.put("category_id", id);

        httpPost(BASE_URL + "api/index.php", httpParams, callback, false, "");
    }

    public void authenticateUser(String email, String password, HttpResponseCallback callback) {

        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put("tag", "login");
        httpParams.put("email", email);
        httpParams.put("password", password);

        httpPost(BASE_URL + "api/index.php", httpParams, callback, false, "");
    }

    public void verifyUser(String cnic, String reg, HttpResponseCallback callback) {

        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put("tag", "auth");
        httpParams.put("cnic", cnic);
        httpParams.put("reg", reg);

        httpPost(BASE_URL + "api/index.php", httpParams, callback, false, "");
    }

    public void getNotification(String pharmacistId, HttpResponseCallback callback) {

        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put("tag", "notification");
        httpParams.put("pharmacist_id", pharmacistId);

        httpPost(BASE_URL + "api/index.php", httpParams, callback, false, "");
    }

    public void licenseCheck(String number, String id, String access, HttpResponseCallback callback) {

        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put("tag", "licence");
        httpParams.put("license_number", number);
        httpParams.put("pharmacist_id", id);
        httpParams.put("access", access);

        httpPost(BASE_URL + "api/index.php", httpParams, callback, false, "");
    }

    public void getCities(String state_id, HttpResponseCallback callback) {

        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put("tag", "city");
        httpParams.put("state_id", state_id);

        httpPost(BASE_URL + "api/index.php", httpParams, callback, false, "");
    }

    public void getTowns(String city_id, HttpResponseCallback callback) {

        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put("tag", "town");
        httpParams.put("city_id", city_id);

        httpPost(BASE_URL + "api/index.php", httpParams, callback, false, "");
    }

    public void getSubCat(String catID, HttpResponseCallback callback) {

        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put("tag", "getsubcategory");
        httpParams.put("category_id", catID);

        httpPost(BASE_URL + "api/index.php", httpParams, callback, false, "");
    }

    public void getOptions(String questionID, HttpResponseCallback callback) {

        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put("tag", "getoption");
        httpParams.put("question_id", questionID);

        httpPost(BASE_URL + "api/index.php", httpParams, callback, false, "");
    }

    public void saveAnswers(String dataToSend, HttpResponseCallback callback) {

        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put("tag", "saveanswers");
        httpParams.put("data", dataToSend);
        httpParams.put("survey_id", sharedPrefUtils.getSharedPrefValue(SURVEY_ID));

        httpPost(BASE_URL + "api/index.php", httpParams, callback, false, "");
    }

    public void saveMainForm(String pharmacist_id, String license_number,
                             String category_id, String state_id,
                             String city_id, String town_id, String name_pos,
                             String address_pos, String gps_location,
                             String name_proprietor,
                             String father_name_proprietor,
                             String cnic_proprietor, String location_nearby, String name_hospital,
                             String working_hours, String email_address,
                             String telephone_number, String address_proprietor, String status,
                             String opt_name_proprietor,
                             String opt_father_name_proprietor,
                             String opt_cnic_proprietor,
                             String opt_address_proprietor, String opt_email_address,
                             String opt_telephone_number, HttpResponseCallback callback) {

        HashMap<String, String> httpParams = new HashMap<>();
        httpParams.put("tag", "survey");
        httpParams.put("pharmacist_id", pharmacist_id);
        httpParams.put("license_number", license_number);
        httpParams.put("category_id", category_id);
        httpParams.put("state_id", state_id);
        httpParams.put("city_id", city_id);
        httpParams.put("town_id", town_id);
        httpParams.put("name_pos", name_pos);
        httpParams.put("address_pos", address_pos);
        httpParams.put("gps_location", gps_location);
        httpParams.put("name_proprietor", name_proprietor);
        httpParams.put("cnic_proprietor", cnic_proprietor);
        httpParams.put("address_proprietor", address_proprietor);
        httpParams.put("location_nearby", location_nearby);
        httpParams.put("name_hospital", name_hospital);
        httpParams.put("working_hours", working_hours);
        httpParams.put("email_address_proprietor", email_address);
        httpParams.put("telephone_number_proprietor", telephone_number);
        httpParams.put("status", status);
        httpParams.put("father_name_proprietor", father_name_proprietor);
        httpParams.put("opt_name_proprietor", opt_name_proprietor);
        httpParams.put("opt_father_name_proprietor", opt_father_name_proprietor);
        httpParams.put("opt_cnic_proprietor", opt_cnic_proprietor);
        httpParams.put("opt_address_proprietor", opt_address_proprietor);
        httpParams.put("opt_telephone_number_proprietor", opt_telephone_number);
        httpParams.put("opt_email_address_proprietor", opt_email_address);


        httpPost(BASE_URL + "api/index.php", httpParams, callback, false, "");
    }

    public void uploadImage(HttpResponseCallback callback, String filePath) {

        HashMap<String, String> httpParams = new HashMap<>();

        httpParams.put("tag", "upload");
        if (getSurveyId().isEmpty() || getSurveyId().length() < 1) {
            httpParams.put("survey_id", sharedPrefUtils.getSharedPrefValue(SURVEY_ID));
        } else {
            httpParams.put("survey_id", getSurveyId());
        }
        httpMultipartRequest(BASE_URL + "api/index.php", httpParams, filePath, "filename", callback, true);
    }
}
