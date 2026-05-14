package com.health.patient.models;

import java.time.LocalDateTime;

public class Appointment {
    private String appointmentId;
    private String patientId;
    private String doctorName;
    private String department;
    private LocalDateTime appointmentDateTime;
    private String status; // SCHEDULED, COMPLETED, CANCELLED, NO_SHOW
    private String reason;
    private String notes;
    private LocalDateTime createdAt;

    public Appointment() {
        this.createdAt = LocalDateTime.now();
        this.status = "SCHEDULED";
    }

    // Getters and Setters
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isUpcoming() {
        return appointmentDateTime != null && 
               appointmentDateTime.isAfter(LocalDateTime.now()) && 
               "SCHEDULED".equals(status);
    }

    @Override
    public String toString() {
        return String.format("""
            Appointment ID: %s
            Doctor: %s - %s
            Date & Time: %s
            Status: %s
            Reason: %s
            """,
            appointmentId, doctorName, department,
            appointmentDateTime, status, reason
        );
    }
}
