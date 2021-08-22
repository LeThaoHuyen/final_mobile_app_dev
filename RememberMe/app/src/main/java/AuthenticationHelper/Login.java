package AuthenticationHelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rememberme.BarcodeScannerActivity;
import com.example.rememberme.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button callSignUp, login_btn;
    ImageView image;
    TextView logoText, sloganText;
    TextInputLayout username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        callSignUp = findViewById(R.id.signup_screen);
        login_btn = findViewById(R.id.validate_login);

        username = findViewById(R.id.log_username);
        password = findViewById(R.id.log_password);

        callSignUp.setOnClickListener((view) -> {
                Intent intent =  new Intent(Login.this, SignUp.class);
                startActivity(intent);
        });

        login_btn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
                LoginUser(view);
            }
        });
    }

    private Boolean validateUsername()
    {
        String val = username.getEditText().getText().toString();
        if(val.isEmpty())
        {
            username.setError("Field cannot be empty");
            return false;
        }
        else
        {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword()
    {
        String val = password.getEditText().getText().toString();
        if(val.isEmpty())
        {
            password.setError("Field cannot be empty");
            return false;
        }
        else
        {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
    public void LoginUser(View view)
    {
        if (validateUsername() | validatePassword())
        {
            isUser(view);
        }
    }

    private void isUser(View view) {

        final String userEnteredUsername= username.getEditText().getText().toString().trim();
        final String userEnteredPassword= password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);
                    String passFromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if (passFromDB.equals(userEnteredPassword)) {

                        username.setError(null);
                        username.setErrorEnabled(false);

                        String nameFromDB = dataSnapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String usernameFromDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);


                        Intent intent =  new Intent(getApplicationContext(), BarcodeScannerActivity.class);


                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phoneNo", phoneNoFromDB);
                        intent.putExtra("password", passFromDB);

                        startActivity(intent);
                    }
                    else {
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                }
                else
                {
                    username.setError("No such User exists");
                    username.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}