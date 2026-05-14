package com.health.patient.models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MedicalRecord {
    private String recordId;
    private String patientId;
    private LocalDateTime recordDate;
    private String diagnosis;
    private String symptoms;
    private String prescribedMedications;
    private String doctorName;
    private String department;
    private Map<String, String> vitalSigns;
    private String labResults;
    private String followUpRequired;
    private LocalDateTime followUpDate;
    private String notes;

    public MedicalRecord() {
        this.recordDate = LocalDateTime.now();
        this.vitalSigns = new HashMap<>();
    }

    // Getters and Setters
    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public LocalDateTime getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDateTime recordDate) { this.recordDate = recordDate; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public String getPrescribedMedications() { return prescribedMedications; }
    public void setPrescribedMedications(String prescribedMedications) { this.prescribedMedications = prescribedMedications; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Map<String, String> getVitalSigns() { return vitalSigns; }
    public void setVitalSigns(Map<String, String> vitalSigns) { this.vitalSigns = vitalSigns; }

    public String getLabResults() { return labResults; }
    public void setLabResults(String labResults) { this.labResults = labResults; }

    public String getFollowUpRequired() { return followUpRequired; }
    public void setFollowUpRequired(String followUpRequired) { this.followUpRequired = followUpRequired; }

    public LocalDateTime getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(LocalDateTime followUpDate) { this.followUpDate = followUpDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public void addVitalSign(String key, String value) {
        this.vitalSigns.put(key, value);
    }

    @Override
    public String toString() {
        return String.format("""
            Record ID: %s
            Date: %s
            Doctor: %s (%s)
            Diagnosis: %s
            Symptoms: %s
            Medications: %s
            Vital Signs: %s
            Follow Up: %s
            """,
            recordId, recordDate, doctorName, department,
            diagnosis, symptoms, prescribedMedications,
            vitalSigns, followUpRequired
        );
    }
}

