package com.example.rememberme.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    private FloatingActionButton floatButton;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setup();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //loadData();
        //fillCelebList();
        Log.d(TAG, "OnCreate: " + singletonClass.toString());

        /**-----------------------------------------------------**/
        // Todo find UI components
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        floatButton = findViewById(R.id.add_button);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav_view, R.string.close_nav_view);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        /**-----------------------------------------------------**/

        /**---------------------Recycler view-------------------**/
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

      //  nAdapter = new RecyclerViewAdapter((List) singletonClass.getProductList(), this);

        //recyclerView.setAdapter(nAdapter);
        nAdapter = new RecyclerViewAdapter(singletonClass.getProductList(), HomeActivity.this);
        recyclerView.setAdapter(nAdapter);
        /**-----------------------------------------------------**/

        /**----------------Floating button----------------------**/
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BarcodeScannerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setup() {
        singletonClass.setUserID("ew5QKmpCHEPCkAZPChxGKSzf0kw2");
        final String userID = singletonClass.getUserID().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");
        Query checkUser = reference.child(userID).orderByChild("id").equalTo(1);

        if (singletonClass.getCount()!=0)
            singletonClass.clear();


        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            int idProduct = postSnapshot.child("id").getValue(Integer.class);
                            String nameProduct = postSnapshot.child("name").getValue(String.class);
                            String expProduct = postSnapshot.child("date").getValue(String.class);
                            String urlProduct = postSnapshot.child("imageURL").getValue(String.class);
                            String seriProduct = postSnapshot.child("seriNum").getValue(String.class);

                            Product x = new Product(idProduct, nameProduct, expProduct, urlProduct, seriProduct);
                            singletonClass.addItem(x);
                            Log.d(TAG, "Successful gì kì dị");
                            Log.d(TAG, nameProduct);

                        }
                    } else{
                        Log.d(TAG, "No such document");
                    }
                } catch (Exception e){
                    Log.d(TAG, "Fail", e);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //reference.addValueEventListener(changeListener);






       /* checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int idProduct = dataSnapshot.child(userID).child("id").getValue(Integer.class);
                    String nameProduct = dataSnapshot.child(userID).child("name").getValue(String.class);
                    String expProduct = dataSnapshot.child(userID).child("date").getValue(String.class);
                    String urlProduct = dataSnapshot.child(userID).child("imageURL").getValue(String.class);
                    String seriProduct = dataSnapshot.child(userID).child("seriNum").getValue(String.class);

                    Product x = new Product(idProduct,nameProduct, expProduct, urlProduct, seriProduct);
                    singletonClass.addItem(x);
                }
                else
                {
                    Toast.makeText(HomeActivity.this, "Database failed", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });*/
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
        singletonClass.setUserID("ew5QKmpCHEPCkAZPChxGKSzf0kw2");

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
