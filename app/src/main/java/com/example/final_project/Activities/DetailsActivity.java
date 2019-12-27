package com.example.final_project.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.final_project.R;

public class DetailsActivity extends AppCompatActivity {
    private TextView itemName;
    private TextView itemNumber;
    private TextView dateAdded;
    private int contactId;
    private Button editButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemName =  findViewById(R.id.itemNameDet);
        itemNumber = findViewById(R.id.numberDet);
        dateAdded =  findViewById(R.id.dateAddedDet);



        Bundle bundle = getIntent().getExtras();

        if ( bundle != null ) {
            itemName.setText(bundle.getString("name"));
            itemNumber.setText(bundle.getString("number"));
            dateAdded.setText(bundle.getString("date"));
            contactId = bundle.getInt("id");
        }



    }
}
