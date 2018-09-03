package com.transvision.ticketing.extra;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import com.transvision.ticketing.R;
import com.transvision.ticketing.SplashActivity;
import com.transvision.ticketing.posting.SendingData;
import java.util.ArrayList;
import static com.transvision.ticketing.extra.Constants.LOGIN_SUCCESS;

public class Apk_Notification extends BroadcastReceiver {
    FunctionsCall functionsCall;
    GetSetValues getSetValues;
    SendingData sendingData;
    String curr_version="";
    Context Notification_context;
    private ArrayList<GetSetValues> arrayList;

    //****************************************handler**********************************************************************
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    functionsCall.logStatus("Server Version: "+getSetValues.getApp_version());
                    if (functionsCall.compare(curr_version, getSetValues.getApp_version()))
                        notification(getContext().getApplicationContext());
                    break;
            }
            return false;
        }
    });
//************************************************************************************************************************
    @Override
    public void onReceive(Context context, Intent intent) {
        arrayList = new ArrayList<>();
        Notification_context = context;
        functionsCall = new FunctionsCall();
        getSetValues = new GetSetValues();
        sendingData = new SendingData();

        functionsCall.logStatus("Apk Notification Current Time: "+functionsCall.currentRecpttime());

//*************************************************Current Version*******************************************************
        PackageInfo pInfo;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            curr_version = pInfo.versionName;
            functionsCall.logStatus("Current Version: "+curr_version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//********************************************is InternetOn******************************************************************
        if (functionsCall.isInternetOn((Activity) context)) {
            functionsCall.logStatus("Checking for newer version of Smart Billing application...");
            SendingData.Login login = sendingData.new Login(getSetValues, handler, arrayList);
            login.execute(getSetValues.getUserId(), getSetValues.getPassword());

        } else functionsCall.logStatus("No Internet Connection...");
    }

    private Context getContext() {
        return this.Notification_context;
    }

    //************************************** notification ********************************************************************
    private void notification(Context context) {
        Intent in = new Intent(context, SplashActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, in, 0);
        //build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("Ticketing Tool")
                        .setContentText("New version of Ticketing Tool is available to download")
                        .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                        .setPriority(NotificationCompat.PRIORITY_HIGH) //must give priority to High, Max which will considered as heads-up notification
                        .setContentIntent(pi)
                        .setAutoCancel(true);
        // Gets an instance of the NotificationManager service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //to post your notification to the notification bar with a id. If a notification with same id already exists, it will get replaced with updated information.
        notificationManager.notify(0, builder.build());
    }
}
