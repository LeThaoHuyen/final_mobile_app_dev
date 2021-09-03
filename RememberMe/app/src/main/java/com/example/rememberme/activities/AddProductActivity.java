package com.example.rememberme.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rememberme.R;
import com.example.rememberme.SingletonClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.rememberme.Models.Product;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddProductActivity extends AppCompatActivity {

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
        setContentView(R.layout.fragment_add_product);

        Bundle extras = getIntent().getExtras();
        String serialNum = extras.getString("serialNum");

        btn_ok = findViewById(R.id.btn_add);
        btn_cancel = findViewById(R.id.btn_cancel);
        imageView = findViewById(R.id.product_image);

        getProductInfoFromSerialNum(serialNum);

        btn_ok.setOnClickListener((view) -> {
            et_name = (EditText) findViewById(R.id.edittext_product_name);
            String nameSave = et_name.getText().toString();

            et_expDate = (EditText) findViewById(R.id.edittext_expiry_date);
            String expDateSave = et_expDate.getText().toString();

            et_serialNum = (EditText) findViewById(R.id.edittext_series_ID);
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

    private void getProductInfoFromSerialNum(String serialNum){
        et_serialNum.setText(serialNum);
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("products").document(serialNum);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    et_name.setText(documentSnapshot.getString("name"));
                    Glide.with(getApplicationContext()).load(documentSnapshot.getString("imageUrl")).into(imageView);
                }
            }
        });
    }
}

