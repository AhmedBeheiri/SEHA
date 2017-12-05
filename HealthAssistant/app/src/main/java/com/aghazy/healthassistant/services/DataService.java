package com.aghazy.healthassistant.services;

import com.aghazy.healthassistant.model.MainModel;

import java.util.ArrayList;


public class DataService {

    private static final DataService ourInstance = new DataService();

    public static DataService getInstance() {
        return ourInstance;
    }

    private DataService() {}

    public ArrayList<MainModel> getMainData() {

        ArrayList<MainModel> list = new ArrayList<>();
        list.add(new MainModel("Status", "Online", "dot"));
        list.add(new MainModel("Bluetooth", "Connected", "dot"));
        //list.add(new MainModel("Mode", "WiFi", "wifi"));
        //list.add(new MainModel("Summary", "Safe &\nHealthy", "plus"));
       // list.add(new MainModel("Body Temp.", "36.4°", "temp"));
        list.add(new MainModel("Heart Rate", "86 BpM", "heart"));

        return list;
    }
}
