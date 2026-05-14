package com.health.patient.models;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Patient {
    private String patientId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String contactNumber;
    private String email;
    private String address;
    private String emergencyContact;
    private List<String> allergies;
    private List<String> chronicConditions;
    private List<MedicalRecord> medicalHistory;
    private List<Appointment> appointments;
    private String insuranceProvider;
    private String insuranceNumber;

    public Patient() {
        this.allergies = new ArrayList<>();
        this.chronicConditions = new ArrayList<>();
        this.medicalHistory = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    // Getters and Setters
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public List<String> getAllergies() { return allergies; }
    public void setAllergies(List<String> allergies) { this.allergies = allergies; }

    public List<String> getChronicConditions() { return chronicConditions; }
    public void setChronicConditions(List<String> chronicConditions) { this.chronicConditions = chronicConditions; }

    public List<MedicalRecord> getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(List<MedicalRecord> medicalHistory) { this.medicalHistory = medicalHistory; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    public String getInsuranceProvider() { return insuranceProvider; }
    public void setInsuranceProvider(String insuranceProvider) { this.insuranceProvider = insuranceProvider; }

    public String getInsuranceNumber() { return insuranceNumber; }
    public void setInsuranceNumber(String insuranceNumber) { this.insuranceNumber = insuranceNumber; }

    // Utility Methods
    public int getAge() {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addAllergy(String allergy) {
        this.allergies.add(allergy);
    }

    public void addChronicCondition(String condition) {
        this.chronicConditions.add(condition);
    }

    public void addMedicalRecord(MedicalRecord record) {
        this.medicalHistory.add(record);
    }

    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    @Override
    public String toString() {
        return String.format("""
            ========================================
            PATIENT INFORMATION
            ========================================
            ID: %s
            Name: %s
            Age: %d years
            Gender: %s
            Blood Group: %s
            Contact: %s
            Email: %s
            Address: %s
            Emergency Contact: %s
            Insurance: %s (%s)
            Allergies: %s
            Chronic Conditions: %s
            Total Medical Records: %d
            Total Appointments: %d
            ========================================
            """,
            patientId, getFullName(), getAge(), gender, bloodGroup,
            contactNumber, email, address, emergencyContact,
            insuranceProvider, insuranceNumber,
            String.join(", ", allergies),
            String.join(", ", chronicConditions),
            medicalHistory.size(),
            appointments.size()
        );
    }
}
