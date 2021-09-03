package com.example.rememberme;
import android.app.Application;

import com.example.rememberme.Models.Product;

import java.util.ArrayList;
import java.util.List;



public class SingletonClass {
    private static SingletonClass singleton;
    private static List<Product> productList = new ArrayList<>();
    private static Product currentProduct;
    public static String userID;
    public static int count =0;
    public static SingletonClass getInstance(){
        if (singleton == null){
            singleton = new SingletonClass();
        }
        return singleton;
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(Product currentProduct) {
        SingletonClass.currentProduct = currentProduct;
    }
    /*
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }
    */

    public void clear()
    {
        productList.clear();
    }
    public void addItem(Product x){
        productList.add(x);
        count++;
    }

    public  List<Product> getProductList() {
        return productList;
    }

    public int getProductID()
    {
        return currentProduct.getId();
    }
    public int getCount() { return count; }
    public String getUserID() { return userID; }
    public void setUserID(String userID){ SingletonClass.userID = userID; }
}

/*
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
*/
