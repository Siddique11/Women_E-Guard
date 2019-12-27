package com.example.final_project.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.final_project.Data.DatabaseHandler;
import com.example.final_project.GPSTracker.GPSTracker;
import com.example.final_project.Model.Contact;
import com.example.final_project.R;

import java.util.ArrayList;
import java.util.List;

public class EmergencyFragment extends Fragment {
    View view;
    GPSTracker gps;
    private List<Contact> contactList;
    private Button sendSafe;
    private Button sendEmergency;
    private double lat;
    private double lng;
    private DatabaseHandler db;


    public EmergencyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.control_fragment, container, false);

        sendEmergency = view.findViewById(R.id.sendemergencyBtn);
        sendSafe = view.findViewById(R.id.sendsafeBtn);

        db = new DatabaseHandler(getActivity());
        contactList = new ArrayList<>();
        contactList = db.getAllContacts();

        sendEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gps = new GPSTracker(getActivity());
                if (gps.canGetLocation()) {
                    lat = gps.getLatitude();
                    lng = gps.getLongitude();
                    Log.v("Location", lat + " , and " + lng);
                    sendEmergencySMS(lat, lng);
                } else {
                    gps.showSettingsAlert();
                }

            }
        });

        sendSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSafeSMS();
            }
        });


        return view;

    }

    private void sendEmergencySMS(Double latitude, Double longitude) {
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
            Toast.makeText(getActivity(), "Sms send successfully", Toast.LENGTH_SHORT).show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void sendSafeSMS() {
        try {

            String msg = "I am safe";
            SmsManager smsManager = SmsManager.getDefault();
            for (Contact c : contactList) {
                Log.v("Message", "" + msg);
                Log.v("number", "" + c.getNumber());
                smsManager.sendTextMessage(c.getNumber(), null, msg, null, null);
            }
            Toast.makeText(getActivity(), "Sms send successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
