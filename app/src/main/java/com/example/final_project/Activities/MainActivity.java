package com.example.final_project.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.final_project.Data.DatabaseHandler;
import com.example.final_project.Model.Contact;
import com.example.final_project.R;
import com.example.final_project.Service.LockService;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText nameItem; //Name
    private EditText number; //number
    private Button saveButton;
    private DatabaseHandler db;

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
        setContentView(R.layout.activity_main);

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
        //startService(new Intent(getApplicationContext(), LockService.class));
        foregroundServiceStart();

        db = new DatabaseHandler(this);

        byPassActivity();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                createPopupDialog();
            }
        });
    }

    private void foregroundServiceStart() {
        String input = "E-guard Running";


        Intent serviceIntent = new Intent(this, LockService.class);
        serviceIntent.putExtra("inputExtra", input);

        ContextCompat.startForegroundService(this, serviceIntent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Intent intent = new Intent(this,SaftyActivity.class);
//            startActivity(intent);
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void createPopupDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        nameItem = view.findViewById(R.id.nameItem);
        number = view.findViewById(R.id.phnNumber);
        saveButton = view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Todo: Save to db
                //Todo: Go to next screen

                if (!nameItem.getText().toString().isEmpty() && !number.getText().toString().isEmpty()) {
                    saveContactToDB(v);
                } else if (nameItem.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else if (number.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter Number", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MainActivity.this, "Please enter name & Number", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void saveContactToDB(View v) {

        Contact contact = new Contact();

        String newContact = nameItem.getText().toString();
        String newContactQuantity = number.getText().toString();

        contact.setName(newContact);
        contact.setNumber(newContactQuantity);

        //Save to DB
        db.addNumber(contact);

        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();

        // Log.d("Item Added ID:", String.valueOf(db.getGroceriesCount()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start a new activity
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        }, 1200); //  1 second.

    }

    public void byPassActivity() {
        //Checks if database is empty; if not, then we just
        //go to ListActivity and show all added items

        if (db.getContactsCount() > 0) {
            startActivity(new Intent(MainActivity.this, TestActivity.class));
            finish();
        }

    }


}
