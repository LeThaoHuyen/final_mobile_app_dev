package com.example.rememberme;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;


public class HomeFragment extends AppCompatActivity {

    Button btn_addOne;
    //List<FragmentItem> itemList = new ArrayList<>();
    private static final String TAG = "Item App";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter nAdapter;
    private RecyclerView.LayoutManager layoutManager;

    final singleTonClass itemList = (singleTonClass) getApplicationContext();

    TextView actionEvent;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        loadData();
        //fillCelebList();
        Log.d(TAG, "OnCreate: " + itemList.toString());

        btn_addOne = findViewById(R.id.btn_add);
        btn_addOne.setOnClickListener((view)->{
            Intent intent =  new Intent(this, AddFragmentItem.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        nAdapter = new RecyclerViewAdapter((List)itemList, this);
        recyclerView.setAdapter(nAdapter);

    }

    private void fillCelebList() {
        FragmentItem c1 = new FragmentItem(1,"G-Dragon", "YG Entertainment", "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1","1");
        FragmentItem c2 = new FragmentItem(2,"Daesung", "YG Entertainment", "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1","1");
        FragmentItem c4 = new FragmentItem(4,"TOP", "YG Entertainment", "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1","1");
        FragmentItem c5 = new FragmentItem(5,"Seungri", "YG Entertainment", "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1","1");
        FragmentItem c6 = new FragmentItem(6,"Lisa", "YG Entertainment", "https://i.pinimg.com/736x/5e/fe/da/5efeda3f61e1f446f8716d585ed3d40d.jpg","1");
        FragmentItem c7 = new FragmentItem(7,"Rose", "YG Entertainment", "https://i.pinimg.com/736x/5e/fe/da/5efeda3f61e1f446f8716d585ed3d40d.jpg","1");
        FragmentItem c8 = new FragmentItem(8,"Jennie", "YG Entertainment", "https://i.pinimg.com/736x/5e/fe/da/5efeda3f61e1f446f8716d585ed3d40d.jpg","1");
        FragmentItem c9 = new FragmentItem(9,"Jisoo", "YG Entertainment", "https://i.pinimg.com/736x/5e/fe/da/5efeda3f61e1f446f8716d585ed3d40d.jpg","1");

        FragmentItem.addAll(Arrays.asList(new FragmentItem[]{c1,c2,c4,c5,c6,c7,c8,c9}));

    }

    private void loadData() {

        final String userName = itemList.getUsernameAcc().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("products");
        Query checkUser = reference.orderByChild("username").equalTo(userName);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    itemList.clear();
                    int i = 0;
                    int size = dataSnapshot.child(userName).child("idSave").getValue(Integer.class);

                    while (i<size)
                    {
                        int idProduct = dataSnapshot.child(userName).child("idSave").getValue(Integer.class);
                        String nameProduct = dataSnapshot.child(userName).child("nameSave").getValue(String.class);
                        String expProduct = dataSnapshot.child(userName).child("expDateSave").getValue(String.class);
                        String urlProduct = dataSnapshot.child(userName).child("URLSave").getValue(String.class);
                        String seriProduct = dataSnapshot.child(userName).child("seriSave").getValue(String.class);

                        FragmentItem x = new FragmentItem(idProduct,nameProduct, expProduct, urlProduct, seriProduct);
                        itemList.addItem(x);
                        i++;
                    }
                }
                else
                {
                    Toast.makeText(HomeFragment.this, "Database failed", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
