package com.example.rememberme;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter nAdapter;
    private RecyclerView.LayoutManager layoutManager;

    final SingletonClass productList = (SingletonClass) getApplicationContext();

    //Todo: declare UI components
    Button btn_addOne;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //loadData();
        //fillCelebList();
        Log.d(TAG, "OnCreate: " + productList.toString());

        // Todo find UI components
        btn_addOne = findViewById(R.id.btn_add);
        btn_addOne.setOnClickListener((view)->{
            Intent intent =  new Intent(this, AddProductActivity.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        nAdapter = new RecyclerViewAdapter((List)productList, this);
        recyclerView.setAdapter(nAdapter);

    }

}
