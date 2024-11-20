package com.ekaltsouni.doctor_appointment_app;
import static com.ekaltsouni.doctor_appointment_app.AppConstants.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientProfile extends AppCompatActivity {

    EditText password, email;
    Button register_button;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        password = findViewById(R.id.password);
        email = findViewById(R.id.email);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        register_button = findViewById(R.id.register_button);


        // If there's a current user signed in, remove the register button
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void signInOrSignUp (View view) {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        if (!userEmail.isEmpty() && !userPassword.isEmpty()) {
            mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        showMessage(PatientProfile.this, "Welcome", "Login successful!");
                        Intent intent = new Intent(PatientProfile.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    showMessage(PatientProfile.this, "Success", "Complete your profile");

                                    user = mAuth.getCurrentUser();
                                    reference = database.getReference("users").child("patients").child(user.getUid());

                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            reference.child("email").setValue(user.getEmail());
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Intent intent = new Intent(PatientProfile.this, EditPatientProfile.class);
                                    startActivity(intent);

                                } else {
                                    showMessage(PatientProfile.this, "Error", "Registration failed: " + task.getException().getMessage());
                                }
                            }
                        });
                    }
                }
            });
        }
        else {
            showMessage(this, "Empty fields", "Please enter your credentials");
        }
    }
}