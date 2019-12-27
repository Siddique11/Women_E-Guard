package com.example.final_project.Model;

public class Contact {

    private String name;
    private String number;
    private String dateItemAdded;
    private int id;

    public Contact() {

    }

    public Contact(String name, String number, String dateItemAdded, int id) {
        this.name = name;
        this.number = number;
        this.dateItemAdded = dateItemAdded;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
