package com.ekaltsouni.doctor_appointment_app;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
    public List<DoctorModel.Doctor> doctorList;

    // Constructor to pass the list of doctors
    public DoctorAdapter(List<DoctorModel.Doctor> doctorList) {
        this.doctorList = doctorList;
    }


    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        // Get the current doctor
        DoctorModel.Doctor doctor = doctorList.get(position);

        // Set the doctor's data in the view holder
        holder.nameTextView.setText(doctor.name);
        holder.specialtyTextView.setText(doctor.specialty);
        holder.areaTextView.setText(doctor.area);
        holder.addressTextView.setText(doctor.address);
        holder.phoneTextView.setText(doctor.phone);

        holder.itemView.findViewById(R.id.buttonViewDocProfile).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DoctorProfile.class);
                intent.putExtra("doctorName", doctor.name);
                intent.putExtra("doctorSpecialty", doctor.specialty);
                intent.putExtra("doctorArea", doctor.area);
                intent.putExtra("doctorAddress", doctor.address);
                intent.putExtra("doctorPhone", doctor.phone);
                intent.putExtra("doctorId", doctor.doctorId); // Pass doctorId
                intent.putExtra("imageUrl", doctor.imageUrl);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the size of the doctor list
        return doctorList.size();
    }

    // ViewHolder class to hold references to each item view
    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, specialtyTextView, areaTextView, addressTextView, phoneTextView;
        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find views by their IDs
            nameTextView = itemView.findViewById(R.id.textViewDoctorName);
            specialtyTextView = itemView.findViewById(R.id.textViewDoctorSpecialty);
            areaTextView = itemView.findViewById(R.id.textViewDoctorArea);
            addressTextView = itemView.findViewById(R.id.textViewDoctorAddress);
            phoneTextView = itemView.findViewById(R.id.textViewDoctorPhone);
        }
    }
}
