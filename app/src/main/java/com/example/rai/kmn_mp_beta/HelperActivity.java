package com.example.rai.kmn_mp_beta;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

public class HelperActivity extends Activity {
    private HelperActivity ctx;
    private MainActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ctx=this;
        ma= MainActivity.getInstance();
        String action = (String) getIntent().getExtras().get("DO");
        if (action.equals("play")) {
            //Your code
            ma.MP_OnPause();
            finish();
        } else if (action.equals("next")) {
            //Your code
            ma.NextSong();
            finish();
        }
         else if (action.equals("app")) {
            //Your code
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(0);
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
