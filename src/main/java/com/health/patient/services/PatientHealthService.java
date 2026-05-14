package com.health.patient.services;

import com.health.patient.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class PatientHealthService {
    private static final Logger logger = LoggerFactory.getLogger(PatientHealthService.class);
    
    private Map<String, Patient> patientDatabase;
    private Map<String, List<MedicalRecord>> medicalRecordsDatabase;
    private Map<String, List<Appointment>> appointmentsDatabase;
    
    public PatientHealthService() {
        this.patientDatabase = new HashMap<>();
        this.medicalRecordsDatabase = new HashMap<>();
        this.appointmentsDatabase = new HashMap<>();
    }
    
    // Patient Management
    public boolean registerPatient(Patient patient) {
        if (patient == null || StringUtils.isBlank(patient.getPatientId())) {
            logger.error("Invalid patient data");
            return false;
        }
        
        if (patientDatabase.containsKey(patient.getPatientId())) {
            logger.warn("Patient with ID {} already exists", patient.getPatientId());
            return false;
        }
        
        patientDatabase.put(patient.getPatientId(), patient);
        medicalRecordsDatabase.put(patient.getPatientId(), new ArrayList<>());
        appointmentsDatabase.put(patient.getPatientId(), new ArrayList<>());
        
        logger.info("Patient registered successfully: {} - {}", 
            patient.getPatientId(), patient.getFullName());
        return true;
    }
    
    public Patient getPatientById(String patientId) {
        return patientDatabase.get(patientId);
    }
    
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientDatabase.values());
    }
    
    public boolean updatePatient(Patient patient) {
        if (patient == null || !patientDatabase.containsKey(patient.getPatientId())) {
            logger.error("Patient not found for update");
            return false;
        }
        
        patientDatabase.put(patient.getPatientId(), patient);
        logger.info("Patient updated: {}", patient.getPatientId());
        return true;
    }
    
    public boolean deletePatient(String patientId) {
        if (!patientDatabase.containsKey(patientId)) {
            logger.error("Patient not found: {}", patientId);
            return false;
        }
        
        patientDatabase.remove(patientId);
        medicalRecordsDatabase.remove(patientId);
        appointmentsDatabase.remove(patientId);
        
        logger.info("Patient deleted: {}", patientId);
        return true;
    }
    
    public List<Patient> searchPatients(String keyword) {
        return patientDatabase.values().stream()
            .filter(p -> p.getFullName().toLowerCase().contains(keyword.toLowerCase()) ||
                         p.getPatientId().toLowerCase().contains(keyword.toLowerCase()) ||
                         p.getContactNumber().contains(keyword))
            .collect(Collectors.toList());
    }
    
    // Medical Records Management
    public boolean addMedicalRecord(MedicalRecord record) {
        if (record == null || !patientDatabase.containsKey(record.getPatientId())) {
            logger.error("Invalid medical record or patient not found");
            return false;
        }
        
        String recordId = "MR" + System.currentTimeMillis() + 
                          new Random().nextInt(1000);
        record.setRecordId(recordId);
        
        medicalRecordsDatabase.get(record.getPatientId()).add(record);
        
        Patient patient = patientDatabase.get(record.getPatientId());
        patient.addMedicalRecord(record);
        
        logger.info("Medical record added for patient: {}", record.getPatientId());
        return true;
    }
    
    public List<MedicalRecord> getPatientMedicalRecords(String patientId) {
        return medicalRecordsDatabase.getOrDefault(patientId, new ArrayList<>());
    }
    
    public List<MedicalRecord> getMedicalRecordsByDateRange(String patientId, 
                                                            LocalDateTime start, 
                                                            LocalDateTime end) {
        return medicalRecordsDatabase.getOrDefault(patientId, new ArrayList<>())
            .stream()
            .filter(r -> r.getRecordDate().isAfter(start) && 
                         r.getRecordDate().isBefore(end))
            .collect(Collectors.toList());
    }
    
    // Appointment Management
    public boolean scheduleAppointment(Appointment appointment) {
        if (appointment == null || !patientDatabase.containsKey(appointment.getPatientId())) {
            logger.error("Invalid appointment or patient not found");
            return false;
        }
        
        String appointmentId = "APT" + System.currentTimeMillis() + 
                               new Random().nextInt(1000);
        appointment.setAppointmentId(appointmentId);
        
        appointmentsDatabase.get(appointment.getPatientId()).add(appointment);
        
        Patient patient = patientDatabase.get(appointment.getPatientId());
        patient.addAppointment(appointment);
        
        logger.info("Appointment scheduled for patient: {} on {}", 
            appointment.getPatientId(), appointment.getAppointmentDateTime());
        return true;
    }
    
    public List<Appointment> getPatientAppointments(String patientId) {
        return appointmentsDatabase.getOrDefault(patientId, new ArrayList<>());
    }
    
    public List<Appointment> getUpcomingAppointments(String patientId) {
        return appointmentsDatabase.getOrDefault(patientId, new ArrayList<>())
            .stream()
            .filter(Appointment::isUpcoming)
            .sorted(Comparator.comparing(Appointment::getAppointmentDateTime))
            .collect(Collectors.toList());
    }
    
    public boolean cancelAppointment(String patientId, String appointmentId) {
        List<Appointment> appointments = appointmentsDatabase.get(patientId);
        if (appointments == null) return false;
        
        Appointment appointment = appointments.stream()
            .filter(a -> a.getAppointmentId().equals(appointmentId))
            .findFirst()
            .orElse(null);
            
        if (appointment != null) {
            appointment.setStatus("CANCELLED");
            logger.info("Appointment cancelled: {}", appointmentId);
            return true;
        }
        
        return false;
    }
    
    // Analytics and Reports
    public Map<String, Object> getPatientHealthSummary(String patientId) {
        Patient patient = getPatientById(patientId);
        if (patient == null) return null;
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("patientInfo", patient);
        summary.put("totalVisits", medicalRecordsDatabase.getOrDefault(patientId, new ArrayList<>()).size());
        summary.put("totalAppointments", appointmentsDatabase.getOrDefault(patientId, new ArrayList<>()).size());
        summary.put("upcomingAppointments", getUpcomingAppointments(patientId));
        summary.put("chronicConditions", patient.getChronicConditions());
        summary.put("allergies", patient.getAllergies());
        
        // Most common diagnosis
        Map<String, Long> diagnosisCount = medicalRecordsDatabase
            .getOrDefault(patientId, new ArrayList<>())
            .stream()
            .filter(r -> r.getDiagnosis() != null)
            .collect(Collectors.groupingBy(
                MedicalRecord::getDiagnosis,
                Collectors.counting()
            ));
        
        summary.put("commonDiagnosis", diagnosisCount);
        
        return summary;
    }
    
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPatients", patientDatabase.size());
        stats.put("totalMedicalRecords", medicalRecordsDatabase.values().stream()
            .mapToInt(List::size).sum());
        stats.put("totalAppointments", appointmentsDatabase.values().stream()
            .mapToInt(List::size).sum());
        
        // Gender distribution
        long maleCount = patientDatabase.values().stream()
            .filter(p -> "MALE".equalsIgnoreCase(p.getGender())).count();
        long femaleCount = patientDatabase.values().stream()
            .filter(p -> "FEMALE".equalsIgnoreCase(p.getGender())).count();
        
        stats.put("malePatients", maleCount);
        stats.put("femalePatients", femaleCount);
        
        // Age distribution
        Map<String, Long> ageGroups = patientDatabase.values().stream()
            .collect(Collectors.groupingBy(
                p -> {
                    int age = p.getAge();
                    if (age < 18) return "Child (0-17)";
                    else if (age < 40) return "Adult (18-39)";
                    else if (age < 60) return "Middle Age (40-59)";
                    else return "Senior (60+)";
                },
                Collectors.counting()
            ));
        
        stats.put("ageGroups", ageGroups);
        
        return stats;
    }
    
    // Validation Methods
    public boolean isValidPatient(Patient patient) {
        if (patient == null) return false;
        if (StringUtils.isBlank(patient.getPatientId())) return false;
        if (StringUtils.isBlank(patient.getFirstName())) return false;
        if (StringUtils.isBlank(patient.getLastName())) return false;
        if (patient.getDateOfBirth() == null) return false;
        if (!isValidEmail(patient.getEmail())) return false;
        if (!isValidPhoneNumber(patient.getContactNumber())) return false;
        
        return true;
    }
    
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    private boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }
    
    // Generate Patient ID
    public String generatePatientId() {
        return "PAT" + System.currentTimeMillis() + 
               String.format("%03d", new Random().nextInt(1000));
    }
}
