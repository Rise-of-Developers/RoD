package com.rod.rodguild;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sign_In extends AppCompatActivity {

    EditText username,password;
    TextView create_account;
    Button btn_masuk;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username = findViewById(R.id.edt_username);
        password = findViewById(R.id.edt_password);
        btn_masuk = findViewById(R.id.button);
        create_account = findViewById(R.id.create_account);

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String localUsername = username.getText().toString();
                final String localPassword = password.getText().toString();

                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(localUsername);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){

                            //get Password firebase
                            String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();


                            //validasi password dengan pas firebase
                            if(localPassword.equals(passwordFromFirebase)){
                                Toast.makeText(getApplicationContext(), "Selamat datang " + localUsername + " Login sukses",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Password salah",Toast.LENGTH_SHORT).show();
                            }


                        }else{
                            Toast.makeText(getApplicationContext(), "Email tidak ada", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Silahkan masukkan ID dan Pass", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(Sign_In.this,SignUp.class );
                startActivity(pindah);
            }
        });


    }

}
