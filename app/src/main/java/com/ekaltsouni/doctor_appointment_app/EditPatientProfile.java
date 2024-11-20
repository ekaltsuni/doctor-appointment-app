package com.ekaltsouni.doctor_appointment_app;

import static com.ekaltsouni.doctor_appointment_app.AppConstants.showMessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPatientProfile extends AppCompatActivity {

    String firstName, lastName, phone;
    EditText firstNameEdit, lastNameEdit, phoneEdit;
    Button bookButton;
    FirebaseUser user;
    FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_profile);

        bookButton = findViewById(R.id.bookButton);

        firstNameEdit = findViewById(R.id.editTextFirstName);
        lastNameEdit = findViewById(R.id.editTextLastName);
        phoneEdit = findViewById(R.id.editTextPhone);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String userId = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child("patients").child(userId);

        loadUserData(userId);
    }

    public void loadUserData (String userId) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild("firstName")) {
                    firstName = snapshot.child("firstName").getValue(String.class);
                    firstNameEdit.setText(firstName);
                }
                if (snapshot.hasChild("lastName")) {
                    lastName = snapshot.child("lastName").getValue(String.class);
                    lastNameEdit.setText(lastName);
                }
                if (snapshot.hasChild("phone")) {
                    phone = snapshot.child("phone").getValue(String.class);
                    phoneEdit.setText(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showMessage(EditPatientProfile.this, "Error", "Loading data failed: " + error.getMessage());
            }
        });
    }

    public void updateProfile(View view) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showMessage(EditPatientProfile.this, "Success", "Profile updated");
                reference.child("firstName").setValue(firstNameEdit.getText().toString());
                reference.child("lastName").setValue(lastNameEdit.getText().toString());
                reference.child("phone").setValue(phoneEdit.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showMessage(EditPatientProfile.this, "Error", "Updating profile failed: " + error.getMessage());
            }
        });
        Intent intent = new Intent(EditPatientProfile.this, MainActivity.class);
        startActivity(intent);
    }

    public void signout (View view) {
        mAuth.signOut();
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void searchDoc (View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}