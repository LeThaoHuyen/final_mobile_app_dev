package com.example.rememberme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import AuthenticationHelper.UserHelperClass;


public class AddFragmentItem extends AppCompatActivity {

        Button btn_ok, btn_cancel;
        final singleTonClass itemList = (singleTonClass) getApplicationContext();
        FirebaseDatabase rootNode;
        DatabaseReference reference;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_product);


        btn_ok = findViewById(R.id.btn_add);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener((view)->{

                EditText name = (EditText) findViewById(R.id.edittext_product_name);
                String nameSave = name.getText().toString();

                EditText expDate = (EditText) findViewById(R.id.edittext_expiry_date);
                String expDateSave = expDate.getText().toString();

                EditText seri = (EditText) findViewById(R.id.edittext_series_ID);
                String seriSave = seri.getText().toString();

                int idSave = itemList.getCount();

                String URLSave = "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1";
                FragmentItem x = new FragmentItem(idSave,nameSave, expDateSave, URLSave, seriSave);
                itemList.addItem(x);

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("products");

                reference.child(itemList.getUsernameAcc()).setValue(x);

                Intent intent =  new Intent(this, HomeFragment.class);
                startActivity(intent);
        });

        btn_cancel.setOnClickListener((view)->{
                Intent intent =  new Intent(this, HomeFragment.class);
                startActivity(intent);
        });
    }

}