package com.example.final_project.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.final_project.Model.Contact;
import com.example.final_project.Util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context ctx;
    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx = context;
    }


    //CRUD operation: Create, Read, Update, Delete

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.KEY_NAME_ITEM + " TEXT,"
                + Constants.KEY_NUMBER + " TEXT, "
                + Constants.KEY_DATE_NAME + " LONG);";

        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    //Add Number
    public void addNumber(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_NAME_ITEM, contact.getName());
        values.put(Constants.KEY_NUMBER, contact.getNumber());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        //insert the row
        db.insert(Constants.TABLE_NAME, null, values);

        Log.v("SAved", "Saved to DB");

    }

    //Get A number
    private Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        //iterate the database
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID,
                        Constants.KEY_NAME_ITEM, Constants.KEY_NUMBER, Constants.KEY_DATE_NAME},
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }


        Contact contact = new Contact();
        contact.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        contact.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_NAME_ITEM)));
        contact.setNumber(cursor.getString(cursor.getColumnIndex(Constants.KEY_NUMBER)));


        //convert timestamp to readable time
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());
        contact.setDateItemAdded(formattedDate);


        return contact;


    }

    //Get All Contacts
    public List<Contact> getAllContacts() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Contact> contactList = new ArrayList<>();


        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
                Constants.KEY_ID, Constants.KEY_NAME_ITEM, Constants.KEY_NUMBER,
                Constants.KEY_DATE_NAME}, null, null, null, null, Constants.KEY_DATE_NAME + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                contact.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_NAME_ITEM)));
                contact.setNumber(cursor.getString(cursor.getColumnIndex(Constants.KEY_NUMBER)));

                //convert timestamp to readable time
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());
                contact.setDateItemAdded(formatedDate);

                //add contacts to contacts list
                contactList.add(contact);


            } while (cursor.moveToNext());
        }


        return contactList;

    }


    //Update Contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(Constants.KEY_NAME_ITEM, contact.getName());
        values.put(Constants.KEY_NUMBER, contact.getNumber());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        //update row
        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
    }

    //Delete Contact
    public void deleteContacts(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ? ", new String[]{String.valueOf(id)});
        db.close();

    }

    //Count Contacts
    public int getContactsCount() {
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
}
