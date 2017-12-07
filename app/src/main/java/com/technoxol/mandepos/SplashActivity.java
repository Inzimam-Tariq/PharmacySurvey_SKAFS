package com.technoxol.mandepos;

import android.os.Bundle;
import android.os.Handler;

import static com.technoxol.mandepos.AppConstants.ALREADY_LOGIN;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUtils();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPrefUtils.getActivityPrefValue(ALREADY_LOGIN)){
                    utils.startNewActivity(LicenceDetailsActivity.class,null,true);
                } else {
                    utils.startNewActivity(LoginActivity.class,null,true);
                }
            }
        },3000);
    }
}
