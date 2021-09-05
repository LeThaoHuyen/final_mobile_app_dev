package com.example.rememberme.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rememberme.R;
import com.example.rememberme.SingletonClass;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.rememberme.Models.Product;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddProductActivity extends AppCompatActivity {
    private String TAG = "AddProductActivity";

    final SingletonClass productList = SingletonClass.getInstance();
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    // variables
    Button btn_ok, btn_cancel;
    EditText et_name, et_serialNum, et_expDate;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Bundle extras = getIntent().getExtras();
        String serialNum = extras.getString("serialNum");

        btn_ok = findViewById(R.id.btn_add);
        btn_cancel = findViewById(R.id.btn_cancel);
        imageView = findViewById(R.id.product_image);
        et_name = (EditText) findViewById(R.id.edittext_product_name);
        et_expDate = (EditText) findViewById(R.id.edittext_expiry_date);
        et_serialNum = (EditText) findViewById(R.id.edittext_series_ID);

        getProductInfoFromSerialNum(serialNum);

        btn_ok.setOnClickListener((view) -> {
            String nameSave = et_name.getText().toString();
            String expDateSave = et_expDate.getText().toString();
            String seriSave = et_serialNum.getText().toString();
            int idSave = productList.getCount();

            String URLSave = "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1";
            Product x = new Product(idSave, nameSave, expDateSave, URLSave, seriSave);
            productList.addItem(x);

            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("products");

            //reference.child(productList.getUsernameAcc()).setValue(x);

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);

            // Todo: show product info taken from barcode....
            // Todo: print out toast to notify "Add successfully"

        });

        btn_cancel.setOnClickListener((view) -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });


    }

    private void getProductInfoFromSerialNum(String serialNum) {
        et_serialNum.setText(serialNum);
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("products").document(serialNum);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("name");
                        String imageUrl = document.getString("imageUrl");
                        et_name.setText(name);
                        Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Fail", task.getException());
                }
            }
        });
    }

}