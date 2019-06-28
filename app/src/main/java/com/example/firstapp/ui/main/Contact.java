package com.example.firstapp.ui.main;

import java.util.ArrayList;

public class Contact {
    private String mName;
    private int mNumber;

    public Contact(String name,  int number) {
        mName = name;
        mNumber = number;
    }

    public String getName() {
        return mName;
    }

    public int getNumber() {
        return mNumber;
    }

    private static int lastContactId = 0;

    public static ArrayList<Contact> createContactsList(int numContacts) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Contact("Person " + ++lastContactId, lastContactId));
        }

        return contacts;
    }
}
