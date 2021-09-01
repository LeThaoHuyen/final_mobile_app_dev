package com.example.rememberme.UserDatabaseHelper;

import android.widget.Toast;

import androidx.annotation.NonNull;


import com.example.rememberme.SingletonClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
/*
public class UserDatabase {
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");
    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    final SingletonClass productList = (SingletonClass) getApplicationContext();

    private void saveData(){

    }

    private void loadData(){
        DatabaseReference productsRef = reference.child(userID);

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

*/