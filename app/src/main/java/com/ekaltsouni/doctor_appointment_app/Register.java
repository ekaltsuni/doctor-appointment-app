package com.ekaltsouni.doctor_appointment_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void goPatientProfile(View view) {
        Intent intent = new Intent(this, PatientProfile.class);
        startActivity(intent);
    }
}