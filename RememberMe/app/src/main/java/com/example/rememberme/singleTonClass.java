package com.example.rememberme;
import android.app.Application;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class singleTonClass extends Application {

    private static singleTonClass singleton;
    public static List<FragmentItem> itemList = new ArrayList<>();
    public static String usernameAcc;
    public static int count = 0;
    public static singleTonClass getInstance() {
        return singleton;
    }


    public String getUsernameAcc() {
        return usernameAcc;
    }

    public void setUsernameAcc(String usernameAcc) {
        singleTonClass.usernameAcc = usernameAcc;
    }

    public int getCount() {
        return count;
    }

    public void clear()
    {
        itemList.clear();
    }
    public void addItem(FragmentItem x)
    {
        itemList.add(x);
        count++;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }
}