package com.example.final_project.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.final_project.Activities.DetailsActivity;
import com.example.final_project.Data.DatabaseHandler;
import com.example.final_project.Model.Contact;
import com.example.final_project.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Contact> contactItem;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Contact> contactItems) {
        this.context = context;
        contactItem = contactItems;
    }


    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {

        Contact contact = contactItem.get(position);

        holder.contactItemName.setText(contact.getName());
        holder.contactNumber.setText(contact.getNumber());
        holder.dateAdded.setText(contact.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        Log.v("Size", ""+contactItem.size());
        return contactItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView contactItemName;
        public TextView contactNumber;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;


        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;

            contactItemName = view.findViewById(R.id.name);
            contactNumber = view.findViewById(R.id.number);
            dateAdded = view.findViewById(R.id.dateAdded);

            editButton = view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //goto next screen //DetailsActivity
                    int position = getAdapterPosition();

                    Contact contact = contactItem.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name", contact.getName());
                    intent.putExtra("number", contact.getNumber());
                    intent.putExtra("id", contact.getId());
                    intent.putExtra("date", contact.getDateItemAdded());
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.editButton:
                    int position = getAdapterPosition();
                    Contact contact = contactItem.get(position);

                    editItem(contact);

                    break;
                case R.id.deleteButton:
                    position = getAdapterPosition();
                    contact = contactItem.get(position);
                    deleteItem(contact.getId());

                    break;


            }

        }

        private void deleteItem(final int id) {
            //create alert dialog
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = view.findViewById(R.id.noButton);
            Button yesButton = view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();


            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete the item.
                    DatabaseHandler db = new DatabaseHandler(context);
                    //delete item
                    db.deleteContacts(id);
                    contactItem.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();


                }
            });


        }

        private void editItem(final Contact contact) {

            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            String name = contact.getName();
            String number = contact.getNumber();

            final EditText nameItem = view.findViewById(R.id.nameItem);
            nameItem.setText(name);
            final EditText phnNumber =  view.findViewById(R.id.phnNumber);
            phnNumber.setText(number.replace("Number: ", ""));
            final TextView title =  view.findViewById(R.id.title);

            title.setText("Edit Number");
            Button saveButton = view.findViewById(R.id.saveButton);


            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler db = new DatabaseHandler(context);

                    //Update item

                    contact.setName(nameItem.getText().toString());
                    contact.setNumber(phnNumber.getText().toString());

                    if (!nameItem.getText().toString().isEmpty()
                            && !phnNumber.getText().toString().isEmpty()) {
                        db.updateContact(contact);
                        notifyItemChanged(getAdapterPosition(), contact);
                    } else {
                        Snackbar.make(view, "Add Name and Number", Snackbar.LENGTH_LONG).show();
                    }

                    dialog.dismiss();

                }
            });

        }
    }

}
