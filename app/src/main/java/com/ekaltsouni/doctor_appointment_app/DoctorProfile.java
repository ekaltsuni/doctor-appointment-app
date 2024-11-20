package com.ekaltsouni.doctor_appointment_app;

import static com.ekaltsouni.doctor_appointment_app.AppConstants.showMessage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class DoctorProfile extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseUser user;
    FirebaseUser patient;
    DatabaseReference reference;
    DatabaseReference slotReference;
    String doctorId;
    ImageView docImage;
    TextView docName, docSpecialty, docAddress, docPhone, docArea;
    CalendarView calendar;
    String dateSelected;
    Button profileButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        profileButton = findViewById(R.id.profile_button);
        database = FirebaseDatabase.getInstance();

        docName = findViewById(R.id.textViewDoctorName);
        docSpecialty = findViewById(R.id.textViewSpecialty);
        docAddress = findViewById(R.id.textViewAddress);
        docPhone = findViewById(R.id.textViewPhone);
        docArea = findViewById(R.id.textViewArea2);
        calendar = findViewById(R.id.calendarView);
        docImage = findViewById(R.id.imageViewDoctor);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child("doctors").child(userId);

        // Get data from the intent
        doctorId = getIntent().getStringExtra("doctorId");
        String doctorName = getIntent().getStringExtra("doctorName");
        String doctorSpecialty = getIntent().getStringExtra("doctorSpecialty");
        String doctorArea = getIntent().getStringExtra("doctorArea");
        String doctorAddress = getIntent().getStringExtra("doctorAddress");
        String doctorPhone = getIntent().getStringExtra("doctorPhone");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Set the data in the views
        docName.setText(doctorName);
        docSpecialty.setText(doctorSpecialty);
        docArea.setText(doctorArea);
        docAddress.setText(doctorAddress);
        docPhone.setText(doctorPhone);

        // Load the image using Glide
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(docImage);
        }

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dateSelected = year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", dayOfMonth);
            }
        });
        slotReference = FirebaseDatabase.getInstance().getReference("users").child("doctors").child(doctorId).child("appointments");
    }


    public void bookSlot(View view) {
        patient = FirebaseAuth.getInstance().getCurrentUser();
        String patientId = patient.getUid();
        DatabaseReference patientReference = FirebaseDatabase.getInstance().getReference().child("users").child("patients").child(patientId);

        patientReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Extract patient's details
                    String patientFirstName = snapshot.child("firstName").getValue(String.class);
                    String patientLastName = snapshot.child("lastName").getValue(String.class);
                    String patientPhone = snapshot.child("phone").getValue(String.class);

                    // Make sure the date is selected
                    if (dateSelected == null || dateSelected.isEmpty()) {
                        showMessage(DoctorProfile.this, "Error", "Please select a date for the appointment.");
                        return;
                    }

                    // Reference to the selected appointment slot under the doctor
                    DatabaseReference appointmentReference = slotReference.child(dateSelected);

                    // Set the patient details for the appointment
                    appointmentReference.child("patientId").setValue(patientId);
                    appointmentReference.child("patientFirstName").setValue(patientFirstName);
                    appointmentReference.child("patientLastName").setValue(patientLastName);
                    appointmentReference.child("patientPhone").setValue(patientPhone);

                    showMessage(DoctorProfile.this, "Request sent", "The doctor will call you soon to arrange a time for your appointment");
                } else {
                    showMessage(DoctorProfile.this, "Error", "Failed to retrieve patient details.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showMessage(DoctorProfile.this, "Error", error.toString());
            }
        });
    }

    public void goprofile(View view) {
        Intent intent = new Intent(this, EditPatientProfile.class);
        startActivity(intent);
    }
}