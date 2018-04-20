package com.mlijo.aryaym.penjual_mlijo;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.mlijo.aryaym.penjual_mlijo.Base.BaseActivity;
import com.mlijo.aryaym.penjual_mlijo.KelolaPenjualan.DaftarTransaksiActivity;
import com.mlijo.aryaym.penjual_mlijo.Obrolan.ObrolanActivity;
import com.mlijo.aryaym.penjual_mlijo.Utils.Constants;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AryaYM on 29/03/2018.
 */

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // OneSignal Initialization
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        //OneSignal.sendTag(Constants.UID, BaseActivity.getUid());
    }

    private class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler{

        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String launchUrl = result.notification.payload.launchURL; // update docs launchUrl

            String customKey = null;
            String titleTransaksi = null;
            String jenisTransaksi = null;
            int openURL;
            Object activityToLaunch = MainActivity.class;

            if (data != null){
                try {
                    openURL = data.getInt("click_action");
                    Log.d("nilai uid", ""+ customKey);
                    Log.d("nilai url", ""+openURL);
                    //if (openURL == "obrolan"){
                    if (openURL == 1){
                        customKey = data.getString("uid");
                        activityToLaunch = ObrolanActivity.class;
                    }else if (openURL == 2){
                        titleTransaksi = data.getString("title");
                        jenisTransaksi = data.getString("transaksi");
                        activityToLaunch = DaftarTransaksiActivity.class;
                    }else  {
                        Log.d("nilai url", "open url error "+openURL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // The following can be used to open an Activity of your choice.
            // Replace - getApplicationContext() - with any Android Context.
            // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
            Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constants.ID_KONSUMEN, customKey);
            intent.putExtra(Constants.TITLE, titleTransaksi);
            intent.putExtra(Constants.TRANSAKSI, jenisTransaksi);
            Log.d("nilai activity", "= " + activityToLaunch);
            // startActivity(intent);
            startActivity(intent);

            // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
            //   if you are calling startActivity above.
        /*
           <application ...>
             <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
           </application>
        */
        }
    }

}
