package com.ekaltsouni.doctor_appointment_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner specialtySpinner;
    private RecyclerView recyclerView;
    DatabaseReference reference;
    Button searchButton, profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.search_button);
        profileButton = findViewById(R.id.profileButton);

        initializeSpecialtySpinner();

        recyclerView = findViewById(R.id.doctorRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    public void gotoprofile(View view) {
        Intent intent = new Intent(this, EditPatientProfile.class);
        startActivity(intent);
    }


    public void searchDoctors(View view) {
        String selectedSpecialty = specialtySpinner.getSelectedItem().toString();
        reference = FirebaseDatabase.getInstance().getReference("users").child("doctors");

        reference.orderByChild("specialty").equalTo(selectedSpecialty).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DoctorModel.Doctor> filteredDoctors = new ArrayList<>();

                for (DataSnapshot doctorSnapshot : snapshot.getChildren()) {
                    String doctorId = doctorSnapshot.getKey();
                    DoctorModel.Doctor doctor = doctorSnapshot.getValue(DoctorModel.Doctor.class);

                    if (doctor != null) {
                        doctor.doctorId = doctorId;
                        filteredDoctors.add(doctor);
                    }
                }

                // Check if the filteredDoctors list is empty and show a toast if no doctors are found
                if (filteredDoctors.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No doctors available.", Toast.LENGTH_SHORT).show();
                }
                DoctorAdapter adapter = (DoctorAdapter) recyclerView.getAdapter();

                if (adapter == null) {
                    // Set the adapter if it's the first time
                    adapter = new DoctorAdapter(filteredDoctors);
                    recyclerView.setAdapter(adapter);

                } else {
                    // Update the existing adapter
                    adapter.doctorList.clear();
                    adapter.doctorList.addAll(filteredDoctors);
                    adapter.notifyDataSetChanged(); // Notify the adapter of data changes
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Failed to fetch doctors", Toast.LENGTH_SHORT).show();
                }
        });
    }

    private void initializeSpecialtySpinner() {
        specialtySpinner = findViewById(R.id.spinnerSpecialty);
        ArrayList<String> specialties = new ArrayList<>();
        specialties.add("Allergist");
        specialties.add("Cardiologist");
        specialties.add("Dermatologist");
        specialties.add("Endocrinologist");
        specialties.add("Family medicine");
        specialties.add("Gynecologist");
        specialties.add("Opthalmologist");
        specialties.add("Pathologist");
        specialties.add("Pediatrician");
        specialties.add("Psychiatrist");
        specialties.add("Neurologist");
        specialties.add("Other");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, specialties);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialtySpinner.setAdapter(adapter);
    }
}