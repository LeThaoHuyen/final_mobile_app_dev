package com.example.rememberme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rememberme.Models.Product;
import com.example.rememberme.R;
import com.example.rememberme.SingletonClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProductActivity extends AppCompatActivity{

    //List<FragmentItem> itemList = new ArrayList<>();
    private static final String TAG = "Item App";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter nAdapter;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    final SingletonClass itemList = SingletonClass.getInstance();

    Button btn_addOne, btn_cancel;
    TextView actionEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        //fillCelebList();
        Log.d(TAG, "OnCreate: " + itemList.toString());
        EditText name = (EditText) findViewById(R.id.edittext_product_name);
        EditText expDate = (EditText) findViewById(R.id.edittext_expiry_date);
        EditText seri = (EditText) findViewById(R.id.edittext_series_ID);

        name.setText(itemList.getCurrentProduct().getName());
        expDate.setText(itemList.getCurrentProduct().getDate());
        seri.setText(itemList.getCurrentProduct().getSeriNum());

        btn_addOne = findViewById(R.id.btn_add);
        btn_addOne.setOnClickListener((view)->{

            if (itemList.getProductList().contains(itemList.getCurrentProduct()))
            {

                String nameSave = name.getText().toString();


                String expDateSave = expDate.getText().toString();


                String seriSave = seri.getText().toString();

                String URLSave = "https://i2.wp.com/idoltv-website.s3.ap-southeast-1.amazonaws.com/wp-content/uploads/2019/02/18154319/big-bang-members-profile.jpg?fit=700%2C466&ssl=1";
                Product x = new Product(itemList.getProductID(), nameSave, expDateSave, URLSave, seriSave);
                itemList.getProductList().remove(itemList.getCurrentProduct());
                itemList.addItem(x);


                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("products");
                int id = itemList.getProductID();

                //remove that item in database
                //reference.child(itemList.getUserID()).child(String.valueOf(id)).removeValue();

                //reference.child(itemList.getUserID()).setValue(x);


                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);

                // Todo: show product info taken from barcode....
                // Todo: print out toast to notify "Update successfully"
            }



        });

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener((view) -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });
    }

}
