package com.ekaltsouni.doctor_appointment_app;

public class DoctorModel {

    public static class Doctor {
        public String address;
        public String area;
        public String name;
        public String phone;
        public String specialty;
        public String doctorId; // This is the doctorId field
        public String imageUrl;
        public Doctor() {}

        public Doctor(String name, String specialty, String area, String address, String phone) {
            this.name = name;
            this.specialty = specialty;
            this.area = area;
            this.address = address;
            this.phone = phone;
        }
    }
}
