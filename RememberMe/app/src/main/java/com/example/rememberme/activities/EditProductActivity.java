package com.example.rememberme.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rememberme.Models.Product;
import com.example.rememberme.R;
import com.example.rememberme.SetNotificationHelper.AlarmBroadcast;
import com.example.rememberme.SetNotificationHelper.Database.DatabaseClass;
import com.example.rememberme.SetNotificationHelper.Database.EntityClass;
import com.example.rememberme.SingletonClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class EditProductActivity extends AppCompatActivity{

    //List<FragmentItem> itemList = new ArrayList<>();
    private static final String TAG = "Item App";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter nAdapter;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    final SingletonClass itemList = SingletonClass.getInstance();

    String timeToNotify = 9 + ":" + 0;
    DatabaseClass databaseClass;
    String URLSave;

    ImageView imageView;
    EditText et_name, et_serialNum, et_expDate;
    Button btn_addOne, btn_cancel;
    Button btn_date;
    TextView actionEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        //fillCelebList();
        Log.d(TAG, "OnCreate: " + itemList.toString());
        et_name = (EditText) findViewById(R.id.edittext_product_name);
        et_expDate = (EditText) findViewById(R.id.edittext_expiry_date);
        et_serialNum = (EditText) findViewById(R.id.edittext_series_ID);
        imageView = findViewById(R.id.product_image);

        et_name.setText(itemList.getCurrentProduct().getName());
        et_expDate.setText(itemList.getCurrentProduct().getDate());
        et_serialNum.setText(itemList.getCurrentProduct().getSeriNum());
        URLSave = itemList.getCurrentProduct().getImageURL();
        Glide.with(getApplicationContext()).load(URLSave).into(imageView);

        btn_date = findViewById(R.id.btn_date);
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

        btn_addOne = findViewById(R.id.btn_add);
        btn_addOne.setOnClickListener((view)->{
            if (itemList.getProductList().contains(itemList.getCurrentProduct()))
            {
                String nameSave = et_name.getText().toString();
                String expDateSave = et_expDate.getText().toString();
                String seriSave = et_serialNum.getText().toString();

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
                // Todo: print out toast to notify "Update successfully"
                Toast.makeText(this, "Update successfully!", Toast.LENGTH_SHORT).show();

                Product x = new Product(itemList.getProductID(), nameSave, expDateSave, URLSave, seriSave);
                itemList.getProductList().remove(itemList.getCurrentProduct());
                itemList.addItem(x);

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Products");
                int id = itemList.getProductID();

                //remove that item in database
                reference.child(itemList.getUserID()).child(String.valueOf(x.getId())).setValue(x);

                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }

        });

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener((view) -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
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

