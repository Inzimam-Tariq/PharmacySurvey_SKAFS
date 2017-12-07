package com.technoxol.mandepos;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static com.technoxol.mandepos.AppConstants.NOTIF_MSG;
import static com.technoxol.mandepos.AppConstants.NOTIF_TITLE;

public class NotificationActivity extends BaseActivity {

    private TextView notifTitle, notifMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initUtils();
        initViews();

        if (sharedPrefUtils.getSharedPrefValue(NOTIF_TITLE)!=null){

            notifTitle.setText(sharedPrefUtils.getSharedPrefValue(NOTIF_TITLE));
            notifMsg.setText(sharedPrefUtils.getSharedPrefValue(NOTIF_MSG));
        } else {
            notifTitle.setText("No Notification Available...!");
        }
    }

    private void initViews() {

        notifTitle = (TextView) findViewById(R.id.notifTitle);
        notifMsg = (TextView) findViewById(R.id.notifMsg);
    }

    public void onClickBack(View view) {

        utils.startNewActivity(LicenceDetailsActivity.class,null,true);
    }

    @Override
    public void onBackPressed() {
        utils.startNewActivity(LicenceDetailsActivity.class,null,true);
    }
}
