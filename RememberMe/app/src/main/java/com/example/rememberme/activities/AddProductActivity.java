package com.example.rememberme.activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.rememberme.ImagePicker.ImagePickerActivity;
import com.example.rememberme.R;
import com.example.rememberme.SetNotificationHelper.AlarmBroadcast;
import com.example.rememberme.SetNotificationHelper.Database.DatabaseClass;
import com.example.rememberme.SetNotificationHelper.Database.EntityClass;
import com.example.rememberme.SingletonClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.rememberme.Models.Product;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.rememberme.ImagePicker.ImagePickerActivity;
import com.example.rememberme.ImagePicker.MyGlideModule;


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
    //ImageView imageView;
    Button btn_date;

    // image picker
    public static final int REQUEST_IMAGE = 100;

    @BindView(R.id.product_image)
    ImageView productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        btn_ok = findViewById(R.id.btn_add);
        btn_cancel = findViewById(R.id.btn_cancel);
        productImage = findViewById(R.id.product_image);
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

        // Image Picker
        ButterKnife.bind(this);
        loadDefaultProduct();

        // Clearing older images from cache directory
        // don't call this line if you want to choose multiple images in the same activity
        // call this once the bitmap(s) usage is over
        ImagePickerActivity.clearCache(this);

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
                    btn_date.setText(day + "/" + (month + 1) + "/" + year);
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
                        Glide.with(getApplicationContext()).load(URLSave).into(productImage);
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

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    // loading product image from local cache
                    loadProduct(uri.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    // Image Picker
    private void loadProduct(String url) {
        Log.d(TAG, "Image cache path: " + url);

        Glide.with(getApplicationContext()).load(url).into(productImage);

        /*GlideApp.with(this).load(url)
                .into(productImage);*/
        productImage.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private void loadDefaultProduct() {
        Glide.with(getApplicationContext()).load(R.drawable.background_white).into(productImage);

        /*GlideApp.with(this).load(R.drawable.background_white)
                .into(productImage);*/
        productImage.setColorFilter(ContextCompat.getColor(this, R.color.white));
    }

    @OnClick({R.id.img_plus, R.id.product_image})
    void onProfileImageClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Grant Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GoTo SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}

