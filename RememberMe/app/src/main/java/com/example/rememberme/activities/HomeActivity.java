package com.example.rememberme.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rememberme.Models.Product;
import com.example.rememberme.R;
import com.example.rememberme.RecyclerViewAdapter;
import com.example.rememberme.SingletonClass;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "HomeActivity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter nAdapter;
    private RecyclerView.LayoutManager layoutManager;

    final SingletonClass singletonClass = SingletonClass.getInstance();

    //Todo: declare UI components
    //Button btn_addOne;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //loadData();
        fillCelebList();
        Log.d(TAG, "OnCreate: " + singletonClass.toString());

        /*-----------------------------------------------------*/
        // Todo find UI components
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav_view, R.string.close_nav_view);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        /*-----------------------------------------------------*/

        /*---------------------Recycler view-------------------*/
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

      //  nAdapter = new RecyclerViewAdapter((List) singletonClass.getProductList(), this);

        //recyclerView.setAdapter(nAdapter);
        nAdapter = new RecyclerViewAdapter(singletonClass.getProductList(), HomeActivity.this);
        recyclerView.setAdapter(nAdapter);
        /*-----------------------------------------------------*/
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.nav_setting){
            //TODO
        }
        else if (id == R.id.nav_logout){

        }
        else if (id == R.id.nav_support){

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void fillCelebList() {
        Product c1 = new Product(1, "G-Dragon", "YG Entertainment", "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1", "1");

        Product c2 = new Product(2, "Daesung", "YG Entertainment", "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1", "1");
        Product c4 = new Product(4, "TOP", "YG Entertainment", "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1", "1");
        Product c5 = new Product(5, "Seungri", "YG Entertainment", "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1", "1");
        Product c6 = new Product(6, "Lisa", "YG Entertainment", "https://i.pinimg.com/736x/5e/fe/da/5efeda3f61e1f446f8716d585ed3d40d.jpg", "1");
        Product c7 = new Product(7, "Rose", "YG Entertainment", "https://i.pinimg.com/736x/5e/fe/da/5efeda3f61e1f446f8716d585ed3d40d.jpg", "1");
        Product c8 = new Product(8, "Jennie", "YG Entertainment", "https://i.pinimg.com/736x/5e/fe/da/5efeda3f61e1f446f8716d585ed3d40d.jpg", "1");
        Product c9 = new Product(9, "Jisoo", "YG Entertainment", "https://i.pinimg.com/736x/5e/fe/da/5efeda3f61e1f446f8716d585ed3d40d.jpg", "1");

        singletonClass.getProductList().addAll(Arrays.asList(new Product[]{c1,c2,c4,c5,c6,c7,c8,c9}));
    }

}
