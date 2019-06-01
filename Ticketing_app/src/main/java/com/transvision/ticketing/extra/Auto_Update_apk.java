package com.transvision.ticketing.extra;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.transvision.ticketing.R;
import com.transvision.ticketing.posting.FTPAPI;
import java.io.File;
import java.util.Objects;

import static com.transvision.ticketing.extra.Constants.APK_FILE_DOWNLOADED;
import static com.transvision.ticketing.extra.Constants.APK_VERSION;

@SuppressLint("Registered")
public class Auto_Update_apk extends Activity {
    FunctionsCall functionCalls;
    GetSetValues getSetValues;
    ProgressDialog progress;
    FTPAPI ftpapi;
    String update_version = "";
    Context context;

    //****************************************handler**********************************************************************
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case APK_FILE_DOWNLOADED:
                    progress.dismiss();
                    functionCalls.updateApp(Auto_Update_apk.this, new File(functionCalls.filepath("ApkFolder") +
                            File.separator + "Ticketing_app_" + getSetValues.getApp_version() + ".apk"));
                    break;
            }
            return false;
        }
    });

    //*****************************************************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_update_apk);

        functionCalls = new FunctionsCall();
        getSetValues = new GetSetValues();
        ftpapi = new FTPAPI(context);
        progress = new ProgressDialog(this);

        Intent intent = getIntent();
        Bundle bnd = intent.getExtras();
        update_version = Objects.requireNonNull(bnd).getString(APK_VERSION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FTPAPI.Download_apk downloadApk = ftpapi.new Download_apk(handler, progress, update_version);
                downloadApk.execute();
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
