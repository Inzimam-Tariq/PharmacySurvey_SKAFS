package com.technoxol.mandepos;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import static com.technoxol.mandepos.AppConstants.ALREADY_LOGIN;
import static com.technoxol.mandepos.AppConstants.PHARMACIST_ACCESS;
import static com.technoxol.mandepos.AppConstants.PHARMACIST_EMAIL;
import static com.technoxol.mandepos.AppConstants.PHARMACIST_ID;
import static com.technoxol.mandepos.AppConstants.PHARMACIST_IMEI;
import static com.technoxol.mandepos.AppConstants.PHARMACIST_PASSWORD;


public class LoginActivity extends BaseActivity implements HttpResponseCallback {

    private EditText emailET, passwordET;
    private String email, password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUtils();
        initViews();

        progressDialog = new ProgressDialog(this);
    }

    private void initViews() {

        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passwordET);
    }

    public void onCLickLogin(View view) {

        if (utils.isNetworkConnected()){

            email = emailET.getText().toString();
            password = passwordET.getText().toString();

            validateFields(email,password);
        } else {

            utils.showAlertDialoge();
        }
    }

    private void validateFields(String email, String password) {

        if (email.isEmpty()){

            emailET.setError("Required");
            emailET.requestFocus();
        } else if (!utils.validEmail(email)){

            emailET.setError("Invalid email...!");
            emailET.requestFocus();
        } else if(password.isEmpty()){

            passwordET.setError("Required");
            passwordET.requestFocus();
        } else {

            progressDialog.setTitle("Loggin In");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            httpService.authenticateUser(email,password,this);
        }
    }

    @Override
    public void onCompleteHttpResponse(String response, String requestUrl) {

        progressDialog.dismiss();

        Log.e("Response", response);
        if (response == null) {
            utils.showToast("No Response....!");
            return;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);

                if(jsonObject.getBoolean("success")){

                    JSONObject pharmacist = jsonObject.getJSONObject("pharmacist");

                    String pharmacist_id = pharmacist.optString("pharmacist_id"); //": "1",
                    String imei = pharmacist.optString("imei"); //": "357805023984942",
                    String email = pharmacist.optString("email"); //: "wajid@skafs.com",
                    String password = pharmacist.optString("password"); //: "ff41f016dec533c7167533deeae3868ee5e66696",
                    String access = pharmacist.optString("access"); //": "0",

                    sharedPrefUtils.saveSharedPrefValue(PHARMACIST_ID,pharmacist_id);
                    sharedPrefUtils.saveSharedPrefValue(PHARMACIST_IMEI,imei);
                    sharedPrefUtils.saveSharedPrefValue(PHARMACIST_EMAIL,email);
                    sharedPrefUtils.saveSharedPrefValue(PHARMACIST_PASSWORD,password);
                    sharedPrefUtils.saveSharedPrefValue(PHARMACIST_ACCESS,access);

                    sharedPrefUtils.saveActivityPrefValue(ALREADY_LOGIN,true);
                    utils.startNewActivity(VerificationActivity.class,null,true);

                } else {
                    utils.showToast(jsonObject.optString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
