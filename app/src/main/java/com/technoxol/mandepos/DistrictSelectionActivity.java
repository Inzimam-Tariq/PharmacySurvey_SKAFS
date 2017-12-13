package com.technoxol.mandepos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.technoxol.mandepos.models.CategoriesInfo;
import com.technoxol.mandepos.models.CitiesInfo;
import com.technoxol.mandepos.models.StatesInfo;
import com.technoxol.mandepos.models.SubCatInfo;
import com.technoxol.mandepos.models.TownsInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.technoxol.mandepos.AppConstants.LICENSE_NUMBER;
import static com.technoxol.mandepos.AppConstants.LICENSE_RESPONSE;
import static com.technoxol.mandepos.AppConstants.PHARMACIST_ID;
import static com.technoxol.mandepos.AppConstants.REG_STATUS;
import static com.technoxol.mandepos.AppConstants.SURVEY_ID;
import static com.technoxol.mandepos.AppConstants.setSurveyId;
import static com.technoxol.mandepos.AppConstants.subCatInfoList;

public class DistrictSelectionActivity extends BaseActivity {

    private GPSTracker gps;

    private EditText stateET, districtET, townET, salesTypeET,
            namePosET, addressPosET, nearByLocationET,
            proprietorNameET, optProprietorNameET,
            proprietorFatherNameET, optProprietorFatherNameET,
            proprietorContactET, optProprietorContactET,
            proprietorCNICET, optProprietorCNICET,
            proprietorEmailET, optProprietorEmailET,
            proprietorAddressET, optProprietorAddressET;

    private String strPharmacistId, strAccess, strLicenseNumber,
            strStateId, strCityId, strTownId, strCategoryId,
            strStateET, strDistrictET, strTownET, strSalesTypeET,
            strNamePosET, strAddressPosET, strNearByLocationET,
            strWorkingHoursRadio = "", strNearbyLocationRadio = "",
            strProprietorNameET, strOptProprietorNameET,
            strProprietorFatherNameET, strOptProprietorFatherNameET,
            strProprietorContactET, strOptProprietorContactET,
            strProprietorCNICET, strOptProprietorCNICET,
            strProprietorEmailET, strOptProprietorEmailET,
            strProprietorAddressET, strOptProprietorAddressET;

    private RadioButton radioHour1, radioHour2, radioHour3, radioLocation1, radioLocation2, radioLocation3;

    private String strGPSLocation = "";

    private boolean isNull = true;
    private boolean isHoursSelected = false, isLocationSelected = false;

    private List<StatesInfo> statesInfoList;
    private List<CategoriesInfo> categoriesInfoList;
    private List<CitiesInfo> citiesInfoList;
    private List<TownsInfo> townsInfoList;

    private RadioButton eight, sixteen, twentyFour,
            rbClinic, rbHospital, rbIndependent;
    private RadioGroup hoursRadioGroup,locationRadioGroup;

    private double latitude, longitude;

    private ProgressDialog getting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_selection);
        setSurveyId("");

        initUtils();
        initViews();

        getting = new ProgressDialog(DistrictSelectionActivity.this);
        getting.setIcon(R.drawable.skafs_logo);
        getting.setMessage("Please wait...");
        getting.setIndeterminate(false);
        getting.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        getting.setCancelable(false);

        statesInfoList = new ArrayList<>();
        categoriesInfoList = new ArrayList<>();
        citiesInfoList = new ArrayList<>();
        townsInfoList = new ArrayList<>();

        utils.setOnKeyListener(namePosET);
        utils.setOnKeyListener(addressPosET);
        utils.setOnKeyListener(nearByLocationET);
        utils.setOnKeyListener(proprietorNameET);
        utils.setOnKeyListener(proprietorFatherNameET);
        utils.setOnKeyListener(proprietorContactET);
        utils.setOnKeyListener(proprietorCNICET);
        utils.setOnKeyListener(proprietorEmailET);
        utils.setOnKeyListener(proprietorAddressET);
//        utils.setOnKeyListener(namePosET);
//        utils.setOnKeyListener(namePosET);
//        utils.setOnKeyListener(namePosET);
//        utils.setOnKeyListener(namePosET);


        namePosET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (namePosET.getText().toString().isEmpty()) {

                   namePosET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                       @Override
                       public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                           if (actionId == EditorInfo.IME_ACTION_DONE
                                   || actionId == EditorInfo.IME_ACTION_NEXT
                                   || actionId == EditorInfo.IME_ACTION_GO
                                   || actionId == EditorInfo.IME_ACTION_SEND
                                   || actionId == EditorInfo.IME_ACTION_SEARCH) {

                               utils.hideKeyboard(v);
                               namePosET.setClickable(false);
                               namePosET.setFocusable(false);
                               return true;
                           }
                           return false;
                       }
                   });
                    return false;
                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });

        addressPosET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (addressPosET.getText().toString().isEmpty()) {

                    addressPosET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                utils.hideKeyboard(v);
                                addressPosET.setClickable(false);
                                addressPosET.setFocusable(false);
                                return true;
                            }
                            return false;
                        }
                    });
                    return false;
                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });

        nearByLocationET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (nearByLocationET.getText().toString().isEmpty()) {

                    nearByLocationET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                utils.hideKeyboard(v);
                                nearByLocationET.setClickable(false);
                                nearByLocationET.setFocusable(false);
                                return true;
                            }
                            return false;
                        }
                    });
                    return false;
                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });

        proprietorNameET.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (proprietorNameET.getText().toString().isEmpty()) {

                            proprietorNameET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                @Override
                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                    if (actionId == EditorInfo.IME_ACTION_DONE
                                            || actionId == EditorInfo.IME_ACTION_NEXT
                                            || actionId == EditorInfo.IME_ACTION_NEXT) {

                                        utils.hideKeyboard(v);
                                        proprietorNameET.setClickable(false);
                                        proprietorNameET.setFocusable(false);
                                        return true;
                                    }
                                    return false;
                                }
                            });
                            return false;
                        } else {
                            utils.showToast("You can't change it now.....!");
                        }
                        return false;
                    }
                });
        optProprietorNameET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (optProprietorNameET.getText().toString().isEmpty()) {

                    optProprietorNameET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                utils.hideKeyboard(v);
                                optProprietorNameET.setClickable(false);
                                optProprietorNameET.setFocusable(false);
                                return true;
                            }
                            return false;
                        }
                    });
                    return false;
                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });
        proprietorFatherNameET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (proprietorFatherNameET.getText().toString().isEmpty()) {

                    proprietorFatherNameET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                utils.hideKeyboard(v);
                                proprietorFatherNameET.setClickable(false);
                                proprietorFatherNameET.setFocusable(false);
                                return true;
                            }
                            return false;
                        }
                    });
                    return false;
                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });
        optProprietorFatherNameET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (optProprietorFatherNameET.getText().toString().isEmpty()) {

                    optProprietorFatherNameET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                utils.hideKeyboard(v);
                                optProprietorFatherNameET.setClickable(false);
                                optProprietorFatherNameET.setFocusable(false);
                                return true;
                            }
                            return false;
                        }
                    });
                    return false;
                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });
        proprietorContactET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (proprietorContactET.getText().toString().isEmpty()) {

                    proprietorContactET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                utils.hideKeyboard(v);
                                proprietorContactET.setClickable(false);
                                proprietorContactET.setFocusable(false);
                                return true;
                            }
                            return false;
                        }
                    });
                    return false;
                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });
        optProprietorContactET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (optProprietorContactET.getText().toString().isEmpty()) {

                    optProprietorContactET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                utils.hideKeyboard(v);
                                optProprietorContactET.setClickable(false);
                                optProprietorContactET.setFocusable(false);
                                return true;
                            }
                            return false;
                        }
                    });
                    return false;
                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });
        proprietorCNICET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (proprietorCNICET.getText().toString().isEmpty()) {

                    proprietorCNICET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                utils.hideKeyboard(v);
                                proprietorCNICET.setClickable(false);
                                proprietorCNICET.setFocusable(false);
                                return true;
                            }
                            return false;
                        }
                    });
                    return false;
                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });
        optProprietorCNICET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (optProprietorCNICET.getText().toString().isEmpty()) {

                    optProprietorCNICET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                utils.hideKeyboard(v);
                                optProprietorCNICET.setClickable(false);
                                optProprietorCNICET.setFocusable(false);
                                return true;
                            }
                            return false;
                        }
                    });
                    return false;
                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });
        proprietorEmailET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (proprietorEmailET.getText().toString().isEmpty()) {

                    proprietorEmailET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                utils.hideKeyboard(v);
                                proprietorEmailET.setClickable(false);
                                proprietorEmailET.setFocusable(false);
                                return true;
                            }
                            return false;
                        }
                    });
                    return false;
                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });
        optProprietorEmailET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (optProprietorEmailET.getText().toString().isEmpty()) {

                    optProprietorEmailET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                utils.hideKeyboard(v);
                                optProprietorEmailET.setClickable(false);
                                optProprietorEmailET.setFocusable(false);
                                return true;
                            }
                            return false;
                        }
                    });
                    return false;
                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });
        proprietorAddressET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (proprietorAddressET.getText().toString().isEmpty()) {

                    proprietorAddressET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                utils.hideKeyboard(v);
                                proprietorAddressET.setClickable(false);
                                proprietorAddressET.setFocusable(false);
                                return true;
                            }
                            return false;
                        }
                    });
                    return false;
                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });

        optProprietorAddressET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (optProprietorAddressET.getText().toString().isEmpty()) {

                    optProprietorAddressET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                utils.hideKeyboard(v);
                                optProprietorAddressET.setClickable(false);
                                optProprietorAddressET.setFocusable(false);
                                return true;
                            }
                            return false;
                        }
                    });
                    return false;
                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            if (bundle.containsKey(LICENSE_RESPONSE)) {

                try {

                    JSONObject jsonObject = new JSONObject(bundle.getString(LICENSE_RESPONSE));

                    if (jsonObject.getBoolean("success")) {

                        Log.e("Response", LICENSE_RESPONSE);

                        JSONObject survey = jsonObject.getJSONObject("survey");

                        if (survey != null) {

                            isNull = false;
                            strPharmacistId = survey.optString("pharmacist_id");
                            strStateId = survey.optString("state_id"); //": "3",
                            strCityId = survey.optString("city_id"); //: "4239",
                            strTownId = survey.optString("town_id"); //: "1",
                            strLicenseNumber = survey.optString("license_number"); //: "139AAIT112016",
                            strNamePosET = survey.optString("name_pos"); //: "Heart Care System",
                            strAddressPosET = survey.optString("address_pos"); //: "438-G-II Ground Floor Wapda Town Lahore",
                            strProprietorNameET = survey.optString("name_proprietor"); //: "",
                            strProprietorFatherNameET = survey.optString("father_name_proprietor"); //: "",
                            strOptProprietorNameET = survey.optString("opt_name_proprietor");
                            strOptProprietorFatherNameET = survey.optString("opt_father_name_proprietor");
                            strProprietorContactET = survey.optString("telephone_number_proprietor"); //: "",
                            strOptProprietorContactET = survey.optString("opt_telephone_number_proprietor");
                            strProprietorCNICET = survey.optString("cnic_proprietor"); //: "0",
                            strOptProprietorCNICET = survey.optString("opt_cnic_proprietor");
                            strProprietorEmailET = survey.optString("email_address_proprietor"); //: "",
                            strOptProprietorEmailET = survey.optString("opt_email_address_proprietor");
                            strProprietorAddressET = survey.optString("father_name_proprietor");
                            strOptProprietorAddressET = survey.optString("opt_address_proprietor");
                            strCategoryId = survey.optString("category_id"); //: "0",

                            strNearByLocationET = survey.optString("name_hospital"); //: "0",
                            strNearbyLocationRadio = survey.optString("location_nearby"); //: "",
                            strWorkingHoursRadio = survey.optString("working_hours"); //: "",

                            isHoursSelected = true;
                            isLocationSelected = true;

                            namePosET.setText(strNamePosET);
                            addressPosET.setText(strAddressPosET);

                            nearByLocationET.setText(strNearByLocationET);
                            proprietorNameET.setText(strProprietorNameET);
                            proprietorFatherNameET.setText(strProprietorFatherNameET);
                            proprietorContactET.setText(strProprietorContactET);
                            proprietorCNICET.setText(strProprietorCNICET);
                            proprietorEmailET.setText(strProprietorEmailET);
                            proprietorAddressET.setText(strProprietorAddressET);
                            optProprietorNameET.setText(strOptProprietorNameET);
                            optProprietorFatherNameET.setText(strOptProprietorFatherNameET);
                            optProprietorContactET.setText(strOptProprietorContactET);
                            optProprietorCNICET.setText(strOptProprietorCNICET);
                            optProprietorEmailET.setText(strOptProprietorEmailET);
                            optProprietorAddressET.setText(strOptProprietorAddressET);

//                            if (!namePosET.getText().toString().isEmpty()) {
//                                namePosET.setClickable(false);
//                                namePosET.setFocusable(false);
//                            }if (!addressPosET.getText().toString().isEmpty()) {
//                                addressPosET.setClickable(false);
//                                addressPosET.setFocusable(false);
//                            }if (!nearByLocationET.getText().toString().isEmpty()) {
//                                nearByLocationET.setClickable(false);
//                                nearByLocationET.setFocusable(false);
//                            }if (!proprietorNameET.getText().toString().isEmpty()) {
//                                proprietorNameET.setClickable(false);
//                                proprietorNameET.setFocusable(false);
//                            }if (!proprietorFatherNameET.getText().toString().isEmpty()) {
//                                proprietorFatherNameET.setClickable(false);
//                                proprietorFatherNameET.setFocusable(false);
//                            }if (!proprietorContactET.getText().toString().isEmpty()) {
//                                proprietorContactET.setClickable(false);
//                                proprietorContactET.setFocusable(false);
//                            }if (!proprietorCNICET.getText().toString().isEmpty()) {
//                                proprietorCNICET.setClickable(false);
//                                proprietorCNICET.setFocusable(false);
//                            }if (!proprietorEmailET.getText().toString().isEmpty()) {
//                                proprietorEmailET.setClickable(false);
//                                proprietorEmailET.setFocusable(false);
//                            }if (!proprietorAddressET.getText().toString().isEmpty()) {
//                                proprietorAddressET.setClickable(false);
//                                proprietorAddressET.setFocusable(false);
//                            }if (!optProprietorNameET.getText().toString().isEmpty()) {
//                                optProprietorNameET.setClickable(false);
//                                optProprietorNameET.setFocusable(false);
//                            }if (!optProprietorFatherNameET.getText().toString().isEmpty()) {
//                                optProprietorFatherNameET.setClickable(false);
//                                optProprietorFatherNameET.setFocusable(false);
//                            }if (!optProprietorContactET.getText().toString().isEmpty()) {
//                                optProprietorContactET.setClickable(false);
//                                optProprietorContactET.setFocusable(false);
//                            }if (!optProprietorCNICET.getText().toString().isEmpty()) {
//                                optProprietorCNICET.setClickable(false);
//                                optProprietorCNICET.setFocusable(false);
//                            }if (!optProprietorEmailET.getText().toString().isEmpty()) {
//                                optProprietorEmailET.setClickable(false);
//                                optProprietorEmailET.setFocusable(false);
//                            }if (!optProprietorAddressET.getText().toString().isEmpty()) {
//                                optProprietorAddressET.setClickable(false);
//                                optProprietorAddressET.setFocusable(false);
//                            }

//                            if (strWorkingHoursRadio.matches("1")){
//
//                                radioHour1.setChecked(true);
//                                radioHour1.setEnabled(false);
//                                radioHour2.setEnabled(false);
//                                radioHour3.setEnabled(false);
//                                radioHour1.setClickable(false);
//                                radioHour2.setClickable(false);
//                                radioHour3.setClickable(false);
//
//                            } else if (strWorkingHoursRadio.matches("2")){
//                                radioHour2.setChecked(true);
//                                radioHour1.setEnabled(false);
//                                radioHour2.setEnabled(false);
//                                radioHour3.setEnabled(false);
//                                radioHour1.setClickable(false);
//                                radioHour2.setClickable(false);
//                                radioHour3.setClickable(false);
//
//                            } else if (strWorkingHoursRadio.matches("3")){
//                                radioHour3.setChecked(true);
//                                radioHour1.setEnabled(false);
//                                radioHour2.setEnabled(false);
//                                radioHour3.setEnabled(false);
//                                radioHour1.setClickable(false);
//                                radioHour2.setClickable(false);
//                                radioHour3.setClickable(false);
//
//                            }
//
//                            if (strNearbyLocationRadio.matches("1")){
//
//                                radioLocation1.setChecked(true);
//                                radioLocation1.setEnabled(false);
//                                radioLocation2.setEnabled(false);
//                                radioLocation3.setEnabled(false);
//                                radioLocation1.setClickable(false);
//                                radioLocation2.setClickable(false);
//                                radioLocation3.setClickable(false);
//
//                            } else if (strNearbyLocationRadio.matches("2")){
//                                radioLocation2.setChecked(true);
//                                radioLocation1.setEnabled(false);
//                                radioLocation2.setEnabled(false);
//                                radioLocation3.setEnabled(false);
//                                radioLocation1.setClickable(false);
//                                radioLocation2.setClickable(false);
//                                radioLocation3.setClickable(false);
//
//                            } else if (strNearbyLocationRadio.matches("3")){
//                                radioLocation3.setChecked(true);
//                                radioLocation1.setEnabled(false);
//                                radioLocation2.setEnabled(false);
//                                radioLocation3.setEnabled(false);
//                                radioLocation1.setClickable(false);
//                                radioLocation2.setClickable(false);
//                                radioLocation3.setClickable(false);
//
//                            }

                            JSONArray states = jsonObject.getJSONArray("states");
                            for (int i = 0; i < states.length(); i++) {
                                JSONObject data = states.getJSONObject(i);
                                StatesInfo statesInfo = new StatesInfo();
                                statesInfo.setState_id(data.optString("state_id"));
                                statesInfo.setStat_name(data.optString("name"));

                                statesInfoList.add(statesInfo);
                            }

                            for (int i = 0; i < statesInfoList.size(); i++) {
                                if (statesInfoList.get(i).getState_id().matches(strStateId)) {
                                    stateET.setText(statesInfoList.get(i).getStat_name());
                                }
                            }

                            Log.e("State Id", strStateId);

                            httpService.getCities(strStateId, new HttpResponseCallback() {
                                @Override
                                public void onCompleteHttpResponse(String response, String requestUrl) {

                                    Log.e("Cities", response);
                                    if (response == null) {
                                        utils.showToast("No Response....!");
                                        return;
                                    } else {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getBoolean("success")) {

                                                JSONArray cities = jsonObject.getJSONArray("cities");
                                                for (int i = 0; i < cities.length(); i++) {
                                                    JSONObject data = cities.getJSONObject(i);
                                                    CitiesInfo citiesInfo = new CitiesInfo();
                                                    citiesInfo.setCity_id(data.optString("city_id"));
                                                    citiesInfo.setName(data.optString("name"));

                                                    citiesInfoList.add(citiesInfo);
                                                }
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    for (int i = 0; i < citiesInfoList.size(); i++) {
                                        if (citiesInfoList.get(i).getCity_id().matches(strCityId)) {
                                            districtET.setText(citiesInfoList.get(i).getName());
                                        }
                                    }
                                }
                            });

                            httpService.getTowns(strCityId, new HttpResponseCallback() {
                                @Override
                                public void onCompleteHttpResponse(String response, String requestUrl) {
                                    Log.e("Response", response);
                                    if (response == null) {
                                        utils.showToast("No Response....!");
                                        return;
                                    } else {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getBoolean("success")) {

                                                JSONArray towns = jsonObject.getJSONArray("towns");
                                                for (int i = 0; i < towns.length(); i++) {
                                                    JSONObject data = towns.getJSONObject(i);
                                                    TownsInfo townsInfo = new TownsInfo();
                                                    townsInfo.setTown_id(data.optString("town_id"));
                                                    townsInfo.setName(data.optString("name"));

                                                    townsInfoList.add(townsInfo);
                                                }
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    for (int i = 0; i < townsInfoList.size(); i++) {
                                        if (townsInfoList.get(i).getTown_id().matches(strTownId)) {
                                            townET.setText(townsInfoList.get(i).getName());
                                        }
                                    }
                                }
                            });


                            JSONArray categories = jsonObject.getJSONArray("categories");
                            for (int i = 0; i < categories.length(); i++) {
                                JSONObject data = categories.getJSONObject(i);
                                CategoriesInfo categoriesInfo = new CategoriesInfo();
                                categoriesInfo.setCategory_id(data.optString("category_id"));
                                categoriesInfo.setCategory_name(data.optString("category_title"));

                                categoriesInfoList.add(categoriesInfo);
                            }

                            Log.e("categories", categoriesInfoList.toString());

                            for (int i = 0; i < categoriesInfoList.size(); i++) {
                                if (categoriesInfoList.get(i).getCategory_id().matches(strCategoryId)) {
                                    salesTypeET.setText(categoriesInfoList.get(i).getCategory_name());

                                }
                            }
                        } else {
                            isNull = true;
                        }
                    } else {

                        Log.e("response ", bundle.getString(LICENSE_RESPONSE));

                        statesInfoList.clear();

                        JSONArray states = jsonObject.getJSONArray("states");
                        for (int i = 0; i < states.length(); i++) {
                            JSONObject data = states.getJSONObject(i);
                            StatesInfo statesInfo = new StatesInfo();
                            statesInfo.setState_id(data.optString("state_id"));
                            statesInfo.setStat_name(data.optString("name"));

                            statesInfoList.add(statesInfo);
                        }


                        JSONArray categories = jsonObject.getJSONArray("categories");
                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject data = categories.getJSONObject(i);
                            CategoriesInfo categoriesInfo = new CategoriesInfo();
                            categoriesInfo.setCategory_id(data.optString("category_id"));
                            categoriesInfo.setCategory_name(data.optString("category_title"));

                            categoriesInfoList.add(categoriesInfo);
                        }

                        isHoursSelected = false;
                        isLocationSelected = false;

                        strPharmacistId = sharedPrefUtils.getSharedPrefValue(PHARMACIST_ID);
                        strLicenseNumber = sharedPrefUtils.getSharedPrefValue(LICENSE_NUMBER);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void freezeRadioButtons(int radio) {
        if (radio==1) {
            utils.showToast("RadioCount = " +hoursRadioGroup.getChildCount());
            for (int i = 0; i < hoursRadioGroup.getChildCount(); i++) {

                hoursRadioGroup.getChildAt(i).setClickable(false);
            }
        }else if (radio==0){
            for (int i = 0; i < locationRadioGroup.getChildCount(); i++) {
                locationRadioGroup.getChildAt(i).setClickable(false);
            }
        }
    }

    private void initViews() {

        hoursRadioGroup = findViewById(R.id.hoursRadioGroup);
        locationRadioGroup = findViewById(R.id.locationRadioGroup);
        radioHour1 = findViewById(R.id.eight);
        radioHour2 = findViewById(R.id.sixteen);
        radioHour3 = findViewById(R.id.twentyFour);
        radioLocation1 = findViewById(R.id.rbClinic);
        radioLocation2 = findViewById(R.id.rbHospital);
        radioLocation3 = findViewById(R.id.rbIndependent);

        stateET = findViewById(R.id.stateET);
        districtET = findViewById(R.id.districtET);
        townET = findViewById(R.id.townET);
        salesTypeET = findViewById(R.id.salesTypeET);
        namePosET = findViewById(R.id.namePosET);
        addressPosET = findViewById(R.id.addressPosET);
        nearByLocationET = findViewById(R.id.nearByLocationET);
        proprietorNameET = findViewById(R.id.proprietorNameET);
        proprietorFatherNameET = findViewById(R.id.proprietorFatherNameET);
        proprietorContactET = findViewById(R.id.proprietorContactET);
        proprietorCNICET = findViewById(R.id.proprietorCNICET);
        proprietorEmailET = findViewById(R.id.proprietorEmailET);
        proprietorAddressET = findViewById(R.id.proprietorAddressET);
        optProprietorNameET = findViewById(R.id.optProprietorNameET);
        optProprietorFatherNameET = findViewById(R.id.optProprietorFatherNameET);
        optProprietorContactET = findViewById(R.id.optProprietorContactET);
        optProprietorCNICET = findViewById(R.id.optProprietorCNICET);
        optProprietorEmailET = findViewById(R.id.optProprietorEmailET);
        optProprietorAddressET = findViewById(R.id.optProprietorAddressET);
    }

    public void stateDialog(View view) {

        if (isNull) {
            if (utils.isNetworkConnected()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final String[] states = new String[statesInfoList.size()];
                for (int i = 0; i < statesInfoList.size(); i++) {
                    states[i] = statesInfoList.get(i).getStat_name();
                }
                builder.setItems(states, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        strStateET = states[which];
                        strStateId = statesInfoList.get(which).getState_id();
                        stateET.setText(strStateET);
                        getting.show();

                        httpService.getCities(statesInfoList.get(which).getState_id(), new HttpResponseCallback() {
                            @Override
                            public void onCompleteHttpResponse(String response, String requestUrl) {
                                if (response == null) {
                                    return;
                                } else {
                                    try {
                                        citiesInfoList.clear();
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getBoolean("success")) {

                                            JSONArray cities = jsonObject.getJSONArray("cities");
                                            for (int i = 0; i < cities.length(); i++) {
                                                JSONObject data = cities.getJSONObject(i);
                                                CitiesInfo citiesInfo = new CitiesInfo();
                                                citiesInfo.setCity_id(data.optString("city_id"));
                                                citiesInfo.setName(data.optString("name"));

                                                citiesInfoList.add(citiesInfo);
                                            }
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                getting.dismiss();
                            }
                        });
                    }
                });
                builder.show();
            } else {
                utils.showAlertDialoge();
            }
        }
    }

    public void districtDialog(View view) {

        if (isNull){
            if (utils.isNetworkConnected()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final String[] cities = new String[citiesInfoList.size()];
                for (int i = 0; i < citiesInfoList.size(); i++) {
                    cities[i] = citiesInfoList.get(i).getName();
                }
                builder.setItems(cities, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        strDistrictET = cities[which];
                        strCityId = citiesInfoList.get(which).getCity_id();
                        districtET.setText(strDistrictET);
                        getting.show();

                        httpService.getTowns(citiesInfoList.get(which).getCity_id(), new HttpResponseCallback() {
                            @Override
                            public void onCompleteHttpResponse(String response, String requestUrl) {
                                if (response == null) {
                                    return;
                                } else {
                                    try {
                                        townsInfoList.clear();
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getBoolean("success")) {

                                            JSONArray towns = jsonObject.getJSONArray("towns");
                                            for (int i = 0; i < towns.length(); i++) {
                                                JSONObject data = towns.getJSONObject(i);
                                                TownsInfo townsInfo = new TownsInfo();
                                                townsInfo.setTown_id(data.optString("town_id"));
                                                townsInfo.setName(data.optString("name"));

                                                townsInfoList.add(townsInfo);
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                getting.dismiss();

                            }
                        });
                    }
                });
                builder.show();
            } else {
                utils.showAlertDialoge();
            }
        }
    }

    public void townDialog(View view) {

        if (isNull) {
            if (utils.isNetworkConnected()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final String[] towns = new String[townsInfoList.size()];
                for (int i = 0; i < townsInfoList.size(); i++) {
                    towns[i] = townsInfoList.get(i).getName();
                }
                builder.setItems(towns, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        strTownET = towns[which];
                        strTownId = townsInfoList.get(which).getTown_id();
                        townET.setText(strTownET);
                    }
                });
                builder.show();
            } else {
                utils.showAlertDialoge();
            }
        }
    }

    public void salesTypeDialog(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] categories = new String[categoriesInfoList.size()];
        for (int i = 0; i < categoriesInfoList.size(); i++){
            categories[i] = categoriesInfoList.get(i).getCategory_name();
        }
        builder.setItems(categories, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                strSalesTypeET = categories[which];
                strCategoryId = categoriesInfoList.get(which).getCategory_id();
                salesTypeET.setText(strSalesTypeET);
            }
        });
        builder.show();
    }

    public void onClickNext(View view) {

        gps = new GPSTracker(this);

        if (utils.isNetworkConnected()){

            if(gps.canGetLocation()){

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                strGPSLocation = String.valueOf(latitude)+","+String.valueOf(longitude);
                Log.e("LocationInDistrict = ", strGPSLocation);

                strStateET = stateET.getText().toString();
                strDistrictET = districtET.getText().toString();
                strTownET = townET.getText().toString();
                strSalesTypeET = salesTypeET.getText().toString();
                strNamePosET = namePosET.getText().toString();
                strAddressPosET = addressPosET.getText().toString();

                strProprietorNameET = proprietorNameET.getText().toString();
                strOptProprietorNameET = optProprietorNameET.getText().toString();
                strProprietorFatherNameET = proprietorFatherNameET.getText().toString();
                strOptProprietorFatherNameET = optProprietorFatherNameET.getText().toString();
                strProprietorContactET = proprietorContactET.getText().toString();
                strOptProprietorContactET = optProprietorContactET.getText().toString();
                strProprietorCNICET = proprietorCNICET.getText().toString();
                strOptProprietorCNICET = optProprietorCNICET.getText().toString();
                strProprietorEmailET = proprietorEmailET.getText().toString();
                strOptProprietorEmailET = optProprietorEmailET.getText().toString();
                strProprietorAddressET = proprietorAddressET.getText().toString();
                strOptProprietorAddressET = optProprietorAddressET.getText().toString();

                strNearByLocationET = nearByLocationET.getText().toString();

                validateFields(strStateET,strDistrictET,strTownET,strSalesTypeET,
                        strNamePosET, strAddressPosET,strGPSLocation,
                        strProprietorContactET,strOptProprietorContactET,strProprietorEmailET,strOptProprietorEmailET,strProprietorNameET,
                        strProprietorFatherNameET,
                        strProprietorCNICET,strOptProprietorCNICET,strProprietorAddressET,strNearByLocationET);
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                utils.showToast("Enable Location....!");
                String message = "GPS is not enabled. Do you want to open settings menu?";
                gps.showSettingsAlert();
            }

        } else {
            utils.showAlertDialoge();
        }
    }

    private void validateFields(String state, String district, String town, final String salesType,
                                String namePos, String addressPos, String strGPS,
                                String contact, String optContact, String email, String optEmail, String proprietor,
                                String proprietorFather,
                                String cnic, String optCnic, String addressProp, String nearByLocation) {

        if (state.isEmpty()){
            stateET.setError("Required");
            stateET.requestFocus();
            utils.showToast("State required....!");
        } else if  (district.isEmpty()){
            districtET.setError("Required");
            districtET.requestFocus();
            utils.showToast("City required....!");
        } else if  (town.isEmpty()){
            townET.setError("Required");
            townET.requestFocus();
            utils.showToast("Town required....!");
        } else if  (salesType.isEmpty()){
            salesTypeET.setError("Required");
            salesTypeET.requestFocus();
            utils.showToast("Sales Type required....!");
        } else if  (namePos.isEmpty()){
            namePosET.setError("Required");
            namePosET.requestFocus();
            utils.showToast("Name POS required....!");
        } else if  (addressPos.isEmpty()){
            addressPosET.setError("Required");
            addressPosET.requestFocus();
            utils.showToast("Address POS required....!");
        } else if  (contact.isEmpty()){
            proprietorContactET.setError("Required");
            proprietorContactET.requestFocus();
            utils.showToast("Proprietor contact required....!");
        } else if  (!utils.validNumber(contact)){
            proprietorContactET.setError("Invalid number...!");
            proprietorContactET.requestFocus();
            utils.showToast("Invalid proprietor contact....!");
        } else if  (!utils.validNumber(optContact) && !optContact.isEmpty()){
            optProprietorContactET.setError("Invalid number...!");
            optProprietorContactET.requestFocus();
            utils.showToast("Invalid optional proprietor contact....!");
        } else if  (email.isEmpty()){
            proprietorEmailET.setError("Required");
            proprietorEmailET.requestFocus();
            utils.showToast("Proprietor email required....!");
        }else if  (proprietor.isEmpty()){
            proprietorNameET.setError("Required");
            proprietorNameET.requestFocus();
            utils.showToast("Proprietor name required....!");
        } else if  (!utils.validName(proprietor)){
            proprietorNameET.setError("Invalid Name");
            proprietorNameET.requestFocus();
            utils.showToast("Invalid proprietor name....!");
        } else if  (proprietorFather.isEmpty()){
            proprietorFatherNameET.setError("Required");
            proprietorFatherNameET.requestFocus();
            utils.showToast("Proprietor father name required....!");
        } else if  (!utils.validName(proprietorFather)){
            proprietorFatherNameET.setError("Invalid Name");
            proprietorFatherNameET.requestFocus();
            utils.showToast("Invalid proprietor father name....!");
        } else if (cnic.isEmpty()){
            proprietorCNICET.setError("Required");
            proprietorCNICET.requestFocus();
            utils.showToast("Proprietor CNIC required....!");
        } else if  (!utils.validCNIC(cnic)){
            proprietorCNICET.setError("Invalid CNIC...!");
            proprietorCNICET.requestFocus();
            utils.showToast("Invalid proprietor CNIC....!");
        }  else if  (!utils.validCNIC(optCnic) && !optCnic.isEmpty()){
            optProprietorCNICET.setError("Invalid CNIC...!");
            optProprietorCNICET.requestFocus();
            utils.showToast("Invalid optional proprietor CNIC....!");
        } else if  (addressProp.isEmpty()){
            proprietorAddressET.setError("Required");
            proprietorAddressET.requestFocus();
            utils.showToast("Proprietor address required....!");
        } else if  (nearByLocation.isEmpty()){
            nearByLocationET.setError("Required");
            nearByLocationET.requestFocus();
            utils.showToast("NearBy Location required....!");
        } else if  (strWorkingHoursRadio.isEmpty()){
            utils.showToast("Working hours are required.....!");
        } else if  (strNearbyLocationRadio.isEmpty()){
            utils.showToast("Nearby location is required.....!");
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Saving Record");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
//
            Log.e("pharmacist_id",strPharmacistId);
            Log.e("License Number",strLicenseNumber);
            Log.e("Category Id",strCategoryId);
            Log.e("State_id",strStateId);
            Log.e("City_id",strCityId);
            Log.e("Town_id",strTownId);
            Log.e("Name POS",strNamePosET);
            Log.e("Address POS",strAddressPosET);
            Log.e("GPS",strGPS);
            Log.e("Proprietor Father",strProprietorFatherNameET);
            Log.e("Proprietor CNIC",strProprietorCNICET);
            Log.e("NearBy Location Radio",strNearbyLocationRadio);
            Log.e("NearBy Location Name",strNearByLocationET);
            Log.e("Working Hours",strWorkingHoursRadio);
            Log.e("Proprietor Email",strProprietorEmailET);
            Log.e("Proprietor Contact",strProprietorContactET);
            Log.e("Proprietor Address",strProprietorAddressET);
            Log.e("Status", "Status"+sharedPrefUtils.getSharedPrefValue(REG_STATUS));
            Log.e("Opt Proprietor Name",strOptProprietorNameET);
            Log.e("Opt Proprietor Father",strOptProprietorFatherNameET);
            Log.e("Opt Proprietor Address",strOptProprietorAddressET);
            Log.e("Opt Proprietor Email",strOptProprietorEmailET);
            Log.e("Opt Proprietor Contact",strOptProprietorContactET);
            httpService.saveMainForm(strPharmacistId, strLicenseNumber,
                        strCategoryId, strStateId, strCityId, strTownId,
                        strNamePosET, strAddressPosET, strGPS,
                        strProprietorNameET,
                    strProprietorFatherNameET,
                    strProprietorCNICET, strNearbyLocationRadio,
                        strNearByLocationET, strWorkingHoursRadio, strProprietorEmailET,
                        strProprietorContactET, strProprietorAddressET,sharedPrefUtils.getSharedPrefValue(REG_STATUS),
                        strOptProprietorNameET,
                    strOptProprietorFatherNameET,
                    strOptProprietorCNICET, strOptProprietorAddressET,
                        strOptProprietorEmailET, strOptProprietorContactET,
                        new HttpResponseCallback() {
                            @Override
                            public void onCompleteHttpResponse(String response, String requestUrl) {
                                if (response == null) {
                                    utils.showToast("No Response....!");
                                    return;
                                } else {
                                    try {
                                        progressDialog.dismiss();
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getBoolean("success")){

                                            utils.showToast(jsonObject.optString("message"));

                                            String surveyId = jsonObject.optString("survey_id");
                                            sharedPrefUtils.saveSharedPrefValue(SURVEY_ID,surveyId);

                                            final ProgressDialog progressDialog1;
                                            progressDialog1 = new ProgressDialog(DistrictSelectionActivity.this);
                                            progressDialog1.setTitle("Getting Survey");
                                            progressDialog1.setMessage("Please wait...");

                                            final List<SubCatInfo> subCatInfosList = new ArrayList<SubCatInfo>();
                                            subCatInfoList.clear();
                                            httpService.getSubCat(strCategoryId, new HttpResponseCallback() {
                                                @Override
                                                public void onCompleteHttpResponse(String response, String requestUrl) {
                                                    if (response == null) {
                                                        utils.showToast("No Response....!");
                                                        return;
                                                    } else {
                                                        try {
                                                            progressDialog1.dismiss();
                                                            JSONObject jsonObject = new JSONObject(response);

                                                            Log.e("response",response);
                                                            if (jsonObject.getBoolean("success")){

                                                                JSONArray subcategories = jsonObject.getJSONArray("subcategories");
                                                                for (int i = 0; i < subcategories.length(); i++){
                                                                    JSONObject cats = subcategories.getJSONObject(i);
                                                                    SubCatInfo subCatInfo = new SubCatInfo();
                                                                    Log.e("cat_id: ",cats.optString("category_id"));
                                                                    subCatInfo.setCategory_id(cats.optString("category_id"));
                                                                    subCatInfo.setCategory_title(cats.optString("category_title"));

                                                                    subCatInfosList.add(subCatInfo);
                                                                }

                                                                subCatInfoList = subCatInfosList;

                                                                utils.startNewActivity(MainActivity.class,null,true);
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            });
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

    public void onClickHours(View view) {

            boolean checked = ((RadioButton) view).isChecked();
            // Check which radio button was clicked
            switch (view.getId()) {
                case R.id.eight:
                    if (checked) {
                        strWorkingHoursRadio = "1";
                        isHoursSelected = true;
                        freezeRadioButtons(1);
                    }
                    break;

                case R.id.sixteen:
                    if (checked) {
                        strWorkingHoursRadio = "2";
                        isHoursSelected = true;
                        freezeRadioButtons(1);
                    }

                    break;

                case R.id.twentyFour:
                    if (checked) {
                        strWorkingHoursRadio = "3";
                        isHoursSelected = true;
                        freezeRadioButtons(1);
                    }
                    break;
            }
    }

    public void onClickLocation(View view) {

            boolean checked = ((RadioButton) view).isChecked();


            switch (view.getId()) {
                case R.id.rbClinic:
                    if (checked) {
                        strNearbyLocationRadio = "1";
                        isLocationSelected = true;
                        freezeRadioButtons(0);
                    }
                    break;
                case R.id.rbHospital:
                    if (checked) {
                        strNearbyLocationRadio = "2";
                        isLocationSelected = true;
                        freezeRadioButtons(0);
                    }
                    break;

                case R.id.rbIndependent:
                    if (checked){
                        strNearbyLocationRadio = "3";
                        isLocationSelected = true;
                        freezeRadioButtons(0);
                    }
                    break;
            }
    }

}
