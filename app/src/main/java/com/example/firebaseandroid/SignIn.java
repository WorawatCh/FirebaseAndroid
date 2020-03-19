package com.example.firebaseandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView emailText, greetingText;
    private Button signOutBtn, saveButton;
    private EditText saveTxt;
    private FirebaseDatabase mRef ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        emailText = (TextView) findViewById(R.id.email);
        greetingText = (TextView) findViewById(R.id.greeting);

//      email.setText(mAuth.getCurrentUser().getEmail());
        signOutBtn =  (Button) findViewById(R.id.signout);
        saveButton = (Button) findViewById(R.id.saveBtn);

        saveTxt = (EditText) findViewById(R.id.saveText);

        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            emailText.setText("Hi ! " + user.getEmail());
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(user.getUid());

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                //                Log.d(TAG, "Value is: " + value);
                greetingText.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(user.getUid());

                String text = saveTxt.getText().toString();
                myRef.setValue(text);
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(SignIn.this, MainActivity.class);
                finish();
                startActivity(i);
            }
        });

    }
}
