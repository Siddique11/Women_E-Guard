package com.example.final_project.BroadcastReceiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.final_project.Data.DatabaseHandler;
import com.example.final_project.GPSTracker.GPSTracker;
import com.example.final_project.Model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ScreenReceiver extends BroadcastReceiver {
    public static boolean wasScreenOn = true;
    static int countPoweroff = 0;
    List<Contact> contactList;
    //    private Context context;
//
//    public ScreenReceiver(Context context){
//        this.context=context;
//    }
    //LocationManager locationManager;

    private DatabaseHandler db;


    public ScreenReceiver() {

    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        GPSTracker gps;
         final double lat;
        final double lng;
        db = new DatabaseHandler(context);
        contactList = new ArrayList<>();
        contactList = db.getAllContacts();


        Log.e("LOB", "onReceive");
        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_OFF:
                // do whatever you need to do here

                wasScreenOn = false;
                countPoweroff++;
                Log.v("LOB", "" + countPoweroff);
                Log.v("LOB", "wasScreenOn" + wasScreenOn);
                break;
            case Intent.ACTION_SCREEN_ON:
                // and do whatever you need to do here
                if (countPoweroff == 3) {

                    gps = new GPSTracker(context);
                    if (gps.canGetLocation()) {
                        lat = gps.getLatitude();
                        lng = gps.getLongitude();
                        Log.v("Location", lat + " , and " + lng);
                        sendSMS(lat, lng);
                    } else {
                        gps.showSettingsAlert();
                    }
                    Log.v("LOB", "userpresent");
                    Log.v("LOB", "wasScreenOn" + wasScreenOn);
                    //String url = "http://www.stackoverflow.com";
                    //Intent i = new Intent(Intent.ACTION_VIEW);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //i.setData(Uri.parse(url));
                    //context.startActivity(i);

                    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vibrator.vibrate(500);
                    }
                    countPoweroff = 0;

                }
                wasScreenOn = true;

                break;
            case Intent.ACTION_USER_PRESENT:
                countPoweroff = 0;
                Log.v("LOB", "" + countPoweroff);

                break;
        }
    }




   private void sendSMS(Double latitude, Double longitude) {
        try {


            String lat = String.valueOf(latitude);
            String lgn = String.valueOf(longitude);
            SmsManager smsManager = SmsManager.getDefault();
            String msg = "I need help, my location is, https://www.google.com/maps/search/?api=1&query=" + lat + "," + lgn;

            for (Contact c : contactList) {
                Log.v("Message", "" + msg);
                Log.v("number", "" + c.getNumber());
                smsManager.sendTextMessage(c.getNumber(), null, msg, null, null);
            }



        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


/*
    public static boolean wasScreenOn = true;
    static int countPowerOff = 0;
    private Activity activity = null;



    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("onReceive", "Power button is pressed.");

        Toast.makeText(context, "power button clicked", Toast.LENGTH_LONG)
                .show();

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            countPowerOff++;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            if (countPowerOff == 5) {
                Log.e("LOB", "userpresent");
                Log.e("LOB", "wasScreenOn" + wasScreenOn);
                String url = "http://www.stackoverflow.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        }

    } */
}