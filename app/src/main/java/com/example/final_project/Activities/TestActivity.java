package com.example.final_project.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.final_project.Data.DatabaseHandler;
import com.example.final_project.GPSTracker.GPSTracker;
import com.example.final_project.Model.Contact;
import com.example.final_project.R;
import com.example.final_project.Service.LockService;
import com.example.final_project.UI.RecyclerViewAdapter;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Contact> contactList;
    private List<Contact> listItems;
    private DatabaseHandler db;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private GPSTracker gps;
    private EditText contactItem;
    private EditText number;
    private Button saveButton;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //permission
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.VIBRATE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.FOREGROUND_SERVICE
        };


        try {
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        tabLayout = findViewById(R.id.tabLayout_id);
        appBarLayout = findViewById(R.id.appbar);
        viewPager = findViewById(R.id.viewPager_id);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());


        viewPagerAdapter.AddFragment(new EmergencyFragment(), "Emergency service");
        viewPagerAdapter.AddFragment(new ListFragment(), "Emergency number");
        viewPagerAdapter.AddFragment(new MapFragment(), "Current Location");


        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            Intent intent = new Intent(this, SaftyActivity.class);
            startActivity(intent);

        } else if (id == R.id.action_police) {
            gps = new GPSTracker(this);
            String lat = Double.toString(gps.getLatitude());
            String lng = Double.toString(gps.getLongitude());
            Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q=police");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            Log.v("Location Data", "" + lat + "" + lng);
            startActivity(mapIntent);
        } else if (id == R.id.close_service) {

            Intent serviceIntent = new Intent(this, LockService.class);
            stopService(serviceIntent);
            finish();
            System.exit(0);


        }

        return super.onOptionsItemSelected(item);
    }
}
