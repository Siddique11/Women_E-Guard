package com.example.final_project.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ListFragment extends Fragment {
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


    public ListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_list, container,false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                createPopDialog();


            }
        });
        db = new DatabaseHandler(getActivity());
        recyclerView = view.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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


        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), listItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        return  view;


    }


    private void createPopDialog() {

        dialogBuilder = new AlertDialog.Builder(getActivity());
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
                    Toast.makeText(getActivity(), "Please enter name", Toast.LENGTH_SHORT).show();

                }else if(number.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter Number", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity(), "Please enter name & Number", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(getActivity(), TestActivity.class));
                getActivity().finish();
            }
        }, 1200); //  1 second.


    }
}
