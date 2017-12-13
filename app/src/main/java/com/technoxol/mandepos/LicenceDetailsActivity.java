package com.technoxol.mandepos;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.technoxol.mandepos.AppConstants.ALREADY_LOGIN;
import static com.technoxol.mandepos.AppConstants.LICENSE_NUMBER;
import static com.technoxol.mandepos.AppConstants.LICENSE_RESPONSE;
import static com.technoxol.mandepos.AppConstants.NOTIF_MSG;
import static com.technoxol.mandepos.AppConstants.NOTIF_TITLE;
import static com.technoxol.mandepos.AppConstants.PHARMACIST_ACCESS;
import static com.technoxol.mandepos.AppConstants.PHARMACIST_ID;
import static com.technoxol.mandepos.AppConstants.REG_STATUS;
import static com.technoxol.mandepos.AppConstants.setSurveyId;


public class LicenceDetailsActivity extends BaseActivity implements HttpResponseCallback {

    private TextView notifText;
    private EditText licenseET;
    private String license, radioValue;

    private RadioGroup radioGroup;
    private RadioButton radioBtns1, radioBtns2;
    private Button surveyBtn;

    private int selectedId;

    private boolean isStausNull;
    private String licenseResponse;
    private String status;
    private Context mContext;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence_details);

        initUtils();
        initViews();
        this.mContext = this;


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Checking");
        progressDialog.setMessage("Please wait...");

        httpService.getNotification(sharedPrefUtils.getSharedPrefValue(PHARMACIST_ID), new HttpResponseCallback() {
            @Override
            public void onCompleteHttpResponse(String response, String requestUrl) {

                if (response == null) {
                    utils.showToast("No Response....!");
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        Log.e("response", response);
                        if (jsonObject.getBoolean("success")) {

                            JSONArray notifications = jsonObject.getJSONArray("notifications");
                            JSONObject data = notifications.getJSONObject(0);
                            sharedPrefUtils.saveSharedPrefValue(NOTIF_TITLE, data.optString("title"));
                            sharedPrefUtils.saveSharedPrefValue(NOTIF_MSG, data.optString("msg"));

                            notifText.setVisibility(View.VISIBLE);

                        } else {
                            notifText.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }


    private void initViews() {

        notifText = findViewById(R.id.notifText);
        licenseET = findViewById(R.id.licenseET);
        surveyBtn = findViewById(R.id.surveyBtn);
        radioGroup = findViewById(R.id.radioBtns);
        radioBtns1 = findViewById(R.id.radioReg);
        radioBtns2 = findViewById(R.id.radioUnReg);

        surveyBtn.setVisibility(View.GONE);
    }

    public void onClickCheck(View view) {

        if (utils.isNetworkConnected()) {
            license = licenseET.getText().toString();
            validateFields(license);
        } else {
            utils.showAlertDialoge();
        }

        utils.hideKeyboard(view);
    }

    private void validateFields(String license) {

        if (license.isEmpty()) {

            licenseET.setError("Required");
            licenseET.requestFocus();
        } else {


            progressDialog.show();

            sharedPrefUtils.saveSharedPrefValue(LICENSE_NUMBER, license);

            Log.e("Id", sharedPrefUtils.getSharedPrefValue(PHARMACIST_ID));
            Log.e("Access", sharedPrefUtils.getSharedPrefValue(PHARMACIST_ACCESS));
            httpService.licenseCheck(license,
                    sharedPrefUtils.getSharedPrefValue(PHARMACIST_ID),
                    sharedPrefUtils.getSharedPrefValue(PHARMACIST_ACCESS), this);
        }
    }

    @Override
    public void onCompleteHttpResponse(String response, String requestUrl) {

        if (response == null) {
            utils.showToast("No Response....!");
        } else {
            try {
                progressDialog.dismiss();
                JSONObject jsonObject = new JSONObject(response);

                licenseResponse = response;

                Log.e("response", response);
                boolean success = jsonObject.getBoolean("success");
                if (success) {

                    JSONObject survey = jsonObject.getJSONObject("survey");

                    if (survey != null) {

                        status = survey.optString("status"); //: "1"
                        sharedPrefUtils.saveSharedPrefValue(REG_STATUS, "1");

                        if (status.matches("1")) {

                            radioBtns1 = findViewById(R.id.radioReg);
                            radioBtns1.setChecked(true);
                            radioValue = radioBtns1.getText().toString();
                            radioBtns1.setEnabled(false);
                            radioBtns1.setClickable(false);
                            radioBtns2.setClickable(false);
                            Bundle bundle = new Bundle();
                            Log.e("Status1", "Here");
                            bundle.putString(LICENSE_RESPONSE, response);
                            utils.startNewActivity(DistrictSelectionActivity.class, bundle, false);
                        } else if (status.matches("2")) {
                            Log.e("Status2", "Here");
                            radioBtns2.setChecked(true);
                            radioValue = radioBtns2.getText().toString();
                            sharedPrefUtils.saveSharedPrefValue(REG_STATUS, "0");
                            radioBtns2.setEnabled(false);
                            radioBtns1.setClickable(false);
                            radioBtns2.setClickable(false);
                        }
                    } else {

                        Log.e("StatusSurveyNull", "Here");
                        sharedPrefUtils.saveSharedPrefValue(REG_STATUS, "1");
                        radioBtns2 = findViewById(R.id.radioUnReg);
                        radioBtns2.setChecked(true);
                        radioValue = radioBtns2.getText().toString();
                        radioBtns1.setClickable(false);
                        radioBtns2.setClickable(false);
                        isStausNull = true;
                    }
                } else {
                    sharedPrefUtils.saveSharedPrefValue(REG_STATUS, "1");
                    radioBtns2 = findViewById(R.id.radioUnReg);
                    radioBtns2.setChecked(true);
                    radioValue = radioBtns2.getText().toString();
                    radioBtns1.setClickable(false);
                    radioBtns2.setClickable(false);
                    isStausNull = true;
                }

                surveyBtn.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onClickSurvey(View view) {

        if (utils.isNetworkConnected()) {
            if (licenseResponse != null) {
                Bundle bundle = new Bundle();
                bundle.putString(LICENSE_RESPONSE, licenseResponse);
                utils.startNewActivity(DistrictSelectionActivity.class, bundle, false);
            } else {
                utils.showToast("Enter License Number First");
            }
        } else {
            utils.showAlertDialoge();
        }
    }

    public void onClickLogout(View view) {

        utils.showToast("Logged Out");
        sharedPrefUtils.saveActivityPrefValue(ALREADY_LOGIN, false);
        utils.startNewActivity(LoginActivity.class, null, true);
    }

    private void freezeRadioButtons() {

        utils.showToast("RadioCount = " + radioGroup.getChildCount());
        for (int i = 0; i < radioGroup.getChildCount(); i++) {

            radioGroup.getChildAt(i).setEnabled(false);


        }
    }

    public void onClickNotif(View view) {
        utils.startNewActivity(NotificationActivity.class, null, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Log.e("WithinPermission", "Location Permission Required");
            ActivityCompat.requestPermissions((AppCompatActivity) mContext,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    101);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {
                Log.e("OnResult", "Permission Granted");
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("OnResult", "Permission Canceled");
        }
    }

    public void gotoTakePicture(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Authentication");
        final EditText pass = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        pass.setHint("Enter Password");
        pass.setLayoutParams(lp);
        builder.setView(pass);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                utils.hideKeyboard(view);
                String val = pass.getText().toString();
                if (!val.isEmpty() && val.length() > 0) {
                    String[] vals = val.split(",");
                    if (vals[0].equals("Inzi769")) {
                        setSurveyId(vals[1]);
                        startActivity(new Intent(mContext, TakePicturesActivity.class));
                    }
                }
            }
        });
        builder.setNeutralButton("Cancel", null);

        // Create the AlertDialog object and return it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
