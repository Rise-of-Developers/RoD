package com.rod.rodguild;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    EditText username, email, password, konfirmasiPassword, status;
    Button btnDaftar;
    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.usernameED);
        email = findViewById(R.id.emailED);
        password = findViewById(R.id.passwordED);
        konfirmasiPassword = findViewById(R.id.konfPassED);
        status = findViewById(R.id.statusED);
        btnDaftar = findViewById(R.id.btn_Daftar);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String localUsername = username.getText().toString();
                final String localEmail = email.getText().toString();
                final String localPassword = password.getText().toString();
                final String localConfirmPass = konfirmasiPassword.getText().toString();
                final String localStatus = status.getText().toString();

                if (TextUtils.isEmpty(localUsername)) {
                    Toast.makeText(getApplicationContext(), "Enter Username!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(localEmail)) {
                    Toast.makeText(getApplicationContext(), "Enter Email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(localEmail).matches()){
                    email.setError("Enter a valid email");
                    email.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(localPassword)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(localConfirmPass)) {
                    Toast.makeText(getApplicationContext(), "Enter Confirm Password!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(localStatus)) {
                    Toast.makeText(getApplicationContext(), "Enter Status!", Toast.LENGTH_SHORT).show();
                    return;
                }


                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, username.getText().toString());
                editor.apply();

                //menyimpan ke Database
                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username.getText().toString());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(getApplicationContext(), "Username sudah dipakai", Toast.LENGTH_SHORT).show();
                        } else {
                            dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                            dataSnapshot.getRef().child("email").setValue(email.getText().toString());
                            dataSnapshot.getRef().child("status").setValue(status.getText().toString());

                            String konfirmasi = konfirmasiPassword.getText().toString();
                            String localPassword = password.getText().toString();
                            if (localPassword.equals(konfirmasi)) {
                                dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                            } else {
                                Toast.makeText(getApplicationContext(), "Password tidak serupa", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    public void kembali(View view) {
        finish();
    }
}
