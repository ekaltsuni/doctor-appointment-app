package com.ekaltsouni.doctor_appointment_app;

import android.app.AlertDialog;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AppConstants {

    public static void showMessage(Context context, String title, String message) {

        new AlertDialog.Builder(context).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}
