package com.technoxol.mandepos;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class VerificationActivity extends BaseActivity{

    private EditText cnicET, regET;
    private String cnic, reg;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        initUtils();
        initViews();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Verifying");
        progressDialog.setMessage("Please wait...");
    }

    private void initViews() {

        cnicET = (EditText) findViewById(R.id.cnicET);
        regET = (EditText) findViewById(R.id.regET);
    }

    public void onClickSubmit(View view) {

        if (utils.isNetworkConnected()){

            cnic = cnicET.getText().toString();
            reg = regET.getText().toString();

            validateFields(cnic, reg);
        } else {
            utils.showAlertDialoge();
        }
    }

    private void validateFields(String cnic, String password) {

        if (cnic.isEmpty()){

            cnicET.setError("Required");
            cnicET.requestFocus();
        } else if (!utils.validCNIC(cnic)){

            cnicET.setError("Invalid CNIC...!");
            cnicET.requestFocus();
        } else if(password.isEmpty()){

            regET.setError("Required");
            regET.requestFocus();
        } else {
            progressDialog.show();
            httpService.verifyUser(this.cnic, reg, new HttpResponseCallback() {
                @Override
                public void onCompleteHttpResponse(String s, String requestUrl) {
                    if (s == null) {
                        utils.showToast("No Response....!");
                        return;
                    } else {
                        try {
                            Log.e("Response", s);
                            progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getBoolean("success")){

                                utils.showToast(jsonObject.optString("message")); //: "Your account has been successfully verified."
//                    utils.startNewActivity(TakePicturesActivity.class,null,true);
                                utils.startNewActivity(LicenceDetailsActivity.class,null,true);
                            } else {

                                utils.showToast(jsonObject.optString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
