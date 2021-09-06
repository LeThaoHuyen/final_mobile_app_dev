package com.example.rememberme.Models;

import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Product {
    private int id;
    private String name;
    private String date;
    private String imageURL;
    private String seriNum;

    public Product () {}
    public Product(int id, String name, String date, String imageURL, String seriNum) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.imageURL = imageURL;
        this.seriNum = seriNum;
    }

    /*
    public String getTimeLeft(){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date startdate = sdf.parse(date);
        Date endDate = Calendar.getInstance().getTime();
        long diff = endDate.getTime() - startDate.getTime();
        System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        return String(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + "days left";
    }*/

    public String getTimeLeft() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date expDate = formatter.parse(date);
            Calendar exp_Date = Calendar.getInstance();
            exp_Date.setTime(expDate);

            Calendar today = Calendar.getInstance();

            long diff = exp_Date.getTimeInMillis() - today.getTimeInMillis();
            long days = diff / (24 * 60 * 60 * 1000);
            String dayDifference = Long.toString(days);

            return (dayDifference + " days left");

        } catch (Exception exception) {
            //Toast.makeText(this, "Unable to find difference", Toast.LENGTH_SHORT).show();
            return "Unable to find difference";
        }
    }

    public String getSeriNum() {
        return seriNum;
    }

    public void setSeriNum(String seriNum) {
        this.seriNum = seriNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() { return date; }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
