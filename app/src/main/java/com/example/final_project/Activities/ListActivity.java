package com.example.final_project.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.final_project.Data.DatabaseHandler;
import com.example.final_project.GPSTracker.GPSTracker;
import com.example.final_project.Model.Contact;
import com.example.final_project.R;
import com.example.final_project.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                createPopDialog();


            }
        });

        db = new DatabaseHandler(this);
        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactList = new ArrayList<>();
        listItems = new ArrayList<>();

        // Get items from database
        contactList = db.getAllContacts();

        for (Contact c : contactList) {
            Contact contact = new Contact();
            contact.setName(c.getName());
            contact.setNumber("Number: " + c.getNumber());

            contact.setId(c.getId());
            contact.setDateItemAdded("Added on: " + c.getDateItemAdded());
            Log.v("Database2", "Name: "+c.getName()+" Number: "+c.getNumber()+" Id: "+c.getId()+" DateAdded: "+c.getDateItemAdded());

            listItems.add(contact);

        }


        recyclerViewAdapter = new RecyclerViewAdapter(ListActivity.this, listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
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
            Intent intent = new Intent(this,SaftyActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.action_police){
            gps = new GPSTracker(this);
            String lat = Double.toString(gps.getLatitude());
            String lng = Double.toString(gps.getLongitude());
            Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng + "?q=police");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            Log.v("Location Data", "" + lat + "" + lng);
            startActivity(mapIntent);
        }

        return super.onOptionsItemSelected(item);
    }




    private void createPopDialog() {

        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        contactItem = view.findViewById(R.id.nameItem);

        number =  view.findViewById(R.id.phnNumber);
        saveButton =  view.findViewById(R.id.saveButton);


        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!contactItem.getText().toString().isEmpty() && !number.getText().toString().isEmpty()) {
                    saveContactToDB(v);
                }else if (contactItem.getText().toString().isEmpty()){
                    Toast.makeText(ListActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                }else if(number.getText().toString().isEmpty()){
                    Toast.makeText(ListActivity.this, "Please enter Number", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(ListActivity.this, "Please enter name & Number", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void saveContactToDB(View v) {

        Contact contact = new Contact();

        String newContact = contactItem.getText().toString();
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
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        }, 1200); //  1 second.


    }

}
