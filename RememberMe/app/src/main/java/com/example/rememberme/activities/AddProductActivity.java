package com.example.rememberme.activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rememberme.R;
import com.example.rememberme.SetNotificationHelper.AlarmBroadcast;
import com.example.rememberme.SetNotificationHelper.Database.DatabaseClass;
import com.example.rememberme.SetNotificationHelper.Database.EntityClass;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddProductActivity extends AppCompatActivity {
    private String TAG = "AddProductActivity";

    final SingletonClass productList = SingletonClass.getInstance();
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    String timeToNotify = 9 + ":" + 0;
    String URLSave = "https://cdn.iconscout.com/icon/free/png-256/delivery-box-1835709-1556268.png";
    DatabaseClass databaseClass;

    // variables
    Button btn_ok, btn_cancel;
    EditText et_name, et_serialNum, et_expDate;
    ImageView imageView;
    Button btn_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        btn_ok = findViewById(R.id.btn_add);
        btn_cancel = findViewById(R.id.btn_cancel);
        imageView = findViewById(R.id.product_image);
        et_name = (EditText) findViewById(R.id.edittext_product_name);
        et_expDate = (EditText) findViewById(R.id.edittext_expiry_date);
        et_serialNum = (EditText) findViewById(R.id.edittext_series_ID);
        //btn_time = findViewById(R.id.btn_time);
        btn_date = findViewById(R.id.btn_date);

        Bundle extras = getIntent().getExtras();
        String serialNum = extras.getString("serialNum");

        if (serialNum != "Barcode") {
            getProductInfoFromSerialNum(serialNum);
        }
        else{
            et_serialNum.setText(serialNum);
        }

        databaseClass = DatabaseClass.getDatabase(getApplicationContext());

        /*btn_time.setOnClickListener((view) -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    timeToNotify = i + ":" + i1;
                    btn_time.setText(FormatTime(i, i1));
                }
            }, hour, minute, false);
            timePickerDialog.show();
        });*/

        btn_date.setOnClickListener((view) -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    btn_date.setText(day + "-" + (month + 1) + "-" + year);
                }
            }, year, month, day);
            datePickerDialog.show();
        });

        btn_ok.setOnClickListener((view) -> {
            String nameSave = et_name.getText().toString();

            /** set Notification **/
            if (btn_date.getText().toString().equals("Select date")) {
                Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show();
            } else {
                EntityClass entityClass = new EntityClass();
                String date = (btn_date.getText().toString().trim());
                String time = FormatTime(9,0);
                //String time = (btn_time.getText().toString().trim());
                entityClass.setEventdate(date);
                entityClass.setEventname(nameSave);
                entityClass.setEventtime(time);
                databaseClass.EventDao().insertAll(entityClass);
                setAlarm(nameSave, date, time);
            }

            String expDateSave = btn_date.getText().toString();
            String seriSave = et_serialNum.getText().toString();
            int idSave = productList.getCount() + 1;

            /** save product **/
            Product x = new Product(idSave, nameSave, expDateSave, URLSave, seriSave);
            productList.addItem(x);

            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("Products");

            //add that product
            reference.child(productList.getUserID()).child(String.valueOf(x.getId())).setValue(x);

            /** return home activity **/
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);

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
                        URLSave = document.getString("imageUrl");
                        et_name.setText(name);
                        Glide.with(getApplicationContext()).load(URLSave).into(imageView);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Fail", task.getException());
                }
            }
        });
    }

    public String FormatTime(int hour, int minute) {

        String time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }

        return time;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                et_name.setText(text.get(0));
            }
        }

    }

    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeToNotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        finish();
    }
}

