package com.health.patient;

import com.health.patient.models.*;
import com.health.patient.services.PatientHealthService;
import com.health.patient.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class App {
    
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final PatientHealthService service = new PatientHealthService();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public static void main(String[] args) {
        logger.info("=== Patient Health Record Management System Started ===");
        System.out.println("\n🏥 WELCOME TO PATIENT HEALTH RECORD MANAGEMENT SYSTEM 🏥\n");
        
        // Load some sample data
        initializeSampleData();
        
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    registerNewPatient();
                    break;
                case 2:
                    viewPatientDetails();
                    break;
                case 3:
                    addMedicalRecord();
                    break;
                case 4:
                    scheduleAppointment();
                    break;
                case 5:
                    viewAppointments();
                    break;
                case 6:
                    generateHealthSummary();
                    break;
                case 7:
                    searchPatients();
                    break;
                case 8:
                    viewSystemStatistics();
                    break;
                case 9:
                    exportPatientData();
                    break;
                case 0:
                    logger.info("System shutdown initiated");
                    System.out.println("\n👋 Thank you for using Patient Health Record System!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please try again.");
            }
        }
    }
    
    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📋 MAIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. 🆕 Register New Patient");
        System.out.println("2. 👤 View Patient Details");
        System.out.println("3. 📝 Add Medical Record");
        System.out.println("4. 📅 Schedule Appointment");
        System.out.println("5. 📋 View Patient Appointments");
        System.out.println("6. 📊 Generate Health Summary");
        System.out.println("7. 🔍 Search Patients");
        System.out.println("8. 📈 View System Statistics");
        System.out.println("9. 💾 Export Patient Data");
        System.out.println("0. 🚪 Exit");
        System.out.println("=".repeat(50));
    }
    
    private static void registerNewPatient() {
        System.out.println("\n--- Register New Patient ---\n");
        
        Patient patient = new Patient();
        patient.setPatientId(service.generatePatientId());
        
        System.out.print("First Name: ");
        patient.setFirstName(scanner.nextLine());
        
        System.out.print("Last Name: ");
        patient.setLastName(scanner.nextLine());
        
        System.out.print("Date of Birth (YYYY-MM-DD): ");
        String dobStr = scanner.nextLine();
        patient.setDateOfBirth(LocalDate.parse(dobStr, dateFormatter));
        
        System.out.print("Gender (MALE/FEMALE/OTHER): ");
        patient.setGender(scanner.nextLine().toUpperCase());
        
        System.out.print("Blood Group: ");
        patient.setBloodGroup(scanner.nextLine().toUpperCase());
        
        System.out.print("Contact Number (10 digits): ");
        patient.setContactNumber(scanner.nextLine());
        
        System.out.print("Email: ");
        patient.setEmail(scanner.nextLine());
        
        System.out.print("Address: ");
        patient.setAddress(scanner.nextLine());
        
        System.out.print("Emergency Contact Number: ");
        patient.setEmergencyContact(scanner.nextLine());
        
        System.out.print("Any Allergies (comma separated): ");
        String allergies = scanner.nextLine();
        if (!allergies.isEmpty()) {
            Arrays.stream(allergies.split(","))
                  .map(String::trim)
                  .forEach(patient::addAllergy);
        }
        
        System.out.print("Chronic Conditions (comma separated): ");
        String conditions = scanner.nextLine();
        if (!conditions.isEmpty()) {
            Arrays.stream(conditions.split(","))
                  .map(String::trim)
                  .forEach(patient::addChronicCondition);
        }
        
        System.out.print("Insurance Provider: ");
        patient.setInsuranceProvider(scanner.nextLine());
        
        System.out.print("Insurance Number: ");
        patient.setInsuranceNumber(scanner.nextLine());
        
        if (service.registerPatient(patient)) {
            System.out.println("\n✅ Patient registered successfully!");
            System.out.println("📌 Patient ID: " + patient.getPatientId());
            logger.info("New patient registered: {}", patient.getPatientId());
        } else {
            System.out.println("\n❌ Failed to register patient!");
        }
    }
    
    private static void viewPatientDetails() {
        System.out.print("\nEnter Patient ID: ");
        String patientId = scanner.nextLine();
        
        Patient patient = service.getPatientById(patientId);
        if (patient != null) {
            System.out.println("\n" + patient);
        } else {
            System.out.println("\n❌ Patient not found!");
        }
    }
    
    private static void addMedicalRecord() {
        System.out.print("\nEnter Patient ID: ");
        String patientId = scanner.nextLine();
        
        Patient patient = service.getPatientById(patientId);
        if (patient == null) {
            System.out.println("❌ Patient not found!");
            return;
        }
        
        System.out.println("\n--- Add Medical Record for: " + patient.getFullName() + " ---\n");
        
        MedicalRecord record = new MedicalRecord();
        record.setPatientId(patientId);
        
        System.out.print("Doctor Name: ");
        record.setDoctorName(scanner.nextLine());
        
        System.out.print("Department: ");
        record.setDepartment(scanner.nextLine());
        
        System.out.print("Symptoms: ");
        record.setSymptoms(scanner.nextLine());
        
        System.out.print("Diagnosis: ");
        record.setDiagnosis(scanner.nextLine());
        
        System.out.print("Prescribed Medications: ");
        record.setPrescribedMedications(scanner.nextLine());
        
        System.out.println("Vital Signs:");
        System.out.print("  Blood Pressure (e.g., 120/80): ");
        String bp = scanner.nextLine();
        if (!bp.isEmpty()) record.addVitalSign("bloodPressure", bp);
        
        System.out.print("  Heart Rate (bpm): ");
        String hr = scanner.nextLine();
        if (!hr.isEmpty()) record.addVitalSign("heartRate", hr);
        
        System.out.print("  Temperature (°C): ");
        String temp = scanner.nextLine();
        if (!temp.isEmpty()) record.addVitalSign("temperature", temp);
        
        System.out.print("Lab Results: ");
        record.setLabResults(scanner.nextLine());
        
        System.out.print("Follow-up Required? (YES/NO): ");
        record.setFollowUpRequired(scanner.nextLine());
        
        if ("YES".equalsIgnoreCase(record.getFollowUpRequired())) {
            System.out.print("Follow-up Date (YYYY-MM-DD HH:MM): ");
            String followUpStr = scanner.nextLine();
            record.setFollowUpDate(LocalDateTime.parse(followUpStr, datetimeFormatter));
        }
        
        System.out.print("Additional Notes: ");
        record.setNotes(scanner.nextLine());
        
        if (service.addMedicalRecord(record)) {
            System.out.println("\n✅ Medical record added successfully!");
            System.out.println("Record ID: " + record.getRecordId());
            logger.info("Medical record added for patient: {}", patientId);
        } else {
            System.out.println("\n❌ Failed to add medical record!");
        }
    }
    
    private static void scheduleAppointment() {
        System.out.print("\nEnter Patient ID: ");
        String patientId = scanner.nextLine();
        
        Patient patient = service.getPatientById(patientId);
        if (patient == null) {
            System.out.println("❌ Patient not found!");
            return;
        }
        
        System.out.println("\n--- Schedule Appointment for: " + patient.getFullName() + " ---\n");
        
        Appointment appointment = new Appointment();
        appointment.setPatientId(patientId);
        
        System.out.print("Doctor Name: ");
        appointment.setDoctorName(scanner.nextLine());
        
        System.out.print("Department: ");
        appointment.setDepartment(scanner.nextLine());
        
        System.out.print("Appointment Date & Time (YYYY-MM-DD HH:MM): ");
        String datetimeStr = scanner.nextLine();
        appointment.setAppointmentDateTime(LocalDateTime.parse(datetimeStr, datetimeFormatter));
        
        System.out.print("Reason for visit: ");
        appointment.setReason(scanner.nextLine());
        
        System.out.print("Additional Notes: ");
        appointment.setNotes(scanner.nextLine());
        
        if (service.scheduleAppointment(appointment)) {
            System.out.println("\n✅ Appointment scheduled successfully!");
            System.out.println("Appointment ID: " + appointment.getAppointmentId());
            logger.info("Appointment scheduled for patient: {}", patientId);
        } else {
            System.out.println("\n❌ Failed to schedule appointment!");
        }
    }
    
    private static void viewAppointments() {
        System.out.print("\nEnter Patient ID: ");
        String patientId = scanner.nextLine();
        
        List<Appointment> appointments = service.getPatientAppointments(patientId);
        if (appointments.isEmpty()) {
            System.out.println("\n📭 No appointments found for this patient.");
            return;
        }
        
        System.out.println("\n--- Appointments ---");
        for (int i = 0; i < appointments.size(); i++) {
            System.out.println("\n📋 Appointment #" + (i + 1));
            System.out.println(appointments.get(i));
            System.out.println("-".repeat(40));
        }
        
        System.out.print("\nWould you like to cancel an appointment? (YES/NO): ");
        if ("YES".equalsIgnoreCase(scanner.nextLine())) {
            System.out.print("Enter Appointment ID to cancel: ");
            String aptId = scanner.nextLine();
            if (service.cancelAppointment(patientId, aptId)) {
                System.out.println("✅ Appointment cancelled successfully!");
            } else {
                System.out.println("❌ Failed to cancel appointment!");
            }
        }
    }
    
    private static void generateHealthSummary() {
        System.out.print("\nEnter Patient ID: ");
        String patientId = scanner.nextLine();
        
        Map<String, Object> summary = service.getPatientHealthSummary(patientId);
        if (summary == null) {
            System.out.println("❌ Patient not found!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 PATIENT HEALTH SUMMARY");
        System.out.println("=".repeat(60));
        
        Patient patient = (Patient) summary.get("patientInfo");
        System.out.println("\n👤 Patient: " + patient.getFullName() + " (" + patient.getPatientId() + ")");
        System.out.println("🎂 Age: " + patient.getAge());
        System.out.println("🩸 Blood Group: " + patient.getBloodGroup());
        
        System.out.println("\n📈 Visit Statistics:");
        System.out.println("  • Total Visits: " + summary.get("totalVisits"));
        System.out.println("  • Total Appointments: " + summary.get("totalAppointments"));
        System.out.println("  • Upcoming Appointments: " + ((List<?>) summary.get("upcomingAppointments")).size());
        
        System.out.println("\n⚠️ Health Conditions:");
        System.out.println("  • Allergies: " + String.join(", ", patient.getAllergies()));
        System.out.println("  • Chronic Conditions: " + String.join(", ", patient.getChronicConditions()));
        
        System.out.println("\n📋 Common Diagnoses:");
        @SuppressWarnings("unchecked")
        Map<String, Long> diagnoses = (Map<String, Long>) summary.get("commonDiagnosis");
        if (diagnoses != null && !diagnoses.isEmpty()) {
            diagnoses.entrySet().stream()
                .limit(5)
                .forEach(e -> System.out.println("  • " + e.getKey() + " (" + e.getValue() + " times)"));
        } else {
            System.out.println("  • No diagnoses recorded");
        }
        
        System.out.println("\n" + "=".repeat(60));
    }
    
    private static void searchPatients() {
        System.out.print("\n🔍 Enter search keyword (name/ID/phone): ");
        String keyword = scanner.nextLine();
        
        List<Patient> results = service.searchPatients(keyword);
        
        if (results.isEmpty()) {
            System.out.println("\n📭 No patients found matching: " + keyword);
            return;
        }
        
        System.out.println("\n--- Search Results (" + results.size() + " found) ---");
        for (Patient patient : results) {
            System.out.println("\n📌 " + patient.getPatientId() + " | " + 
                             patient.getFullName() + " | " + 
                             patient.getContactNumber() + " | Age: " + patient.getAge());
        }
    }
    
    private static void viewSystemStatistics() {
        Map<String, Object> stats = service.getSystemStatistics();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🏥 SYSTEM STATISTICS");
        System.out.println("=".repeat(60));
        
        System.out.println("\n📊 Overview:");
        System.out.println("  • Total Patients: " + stats.get("totalPatients"));
        System.out.println("  • Total Medical Records: " + stats.get("totalMedicalRecords"));
        System.out.println("  • Total Appointments: " + stats.get("totalAppointments"));
        
        System.out.println("\n👥 Demographics:");
        System.out.println("  • Male Patients: " + stats.get("malePatients"));
        System.out.println("  • Female Patients: " + stats.get("femalePatients"));
        
        System.out.println("\n🎂 Age Distribution:");
        @SuppressWarnings("unchecked")
        Map<String, Long> ageGroups = (Map<String, Long>) stats.get("ageGroups");
        ageGroups.forEach((group, count) -> 
            System.out.println("  • " + group + ": " + count));
        
        System.out.println("\n" + "=".repeat(60));
    }
    
    private static void exportPatientData() {
        System.out.print("\nEnter Patient ID to export: ");
        String patientId = scanner.nextLine();
        
        Patient patient = service.getPatientById(patientId);
        if (patient == null) {
            System.out.println("❌ Patient not found!");
            return;
        }
        
        try {
            CSVExporter.exportPatientToCSV(patient, "patient_" + patientId + ".csv");
            System.out.println("✅ Patient data exported to: patient_" + patientId + ".csv");
            
            List<MedicalRecord> records = service.getPatientMedicalRecords(patientId);
            JSONExporter.exportMedicalRecordsToJSON(records, "medical_records_" + patientId + ".json");
            System.out.println("✅ Medical records exported to: medical_records_" + patientId + ".json");
            
            logger.info("Patient data exported for: {}", patientId);
        } catch (Exception e) {
            logger.error("Export failed: {}", e.getMessage());
            System.out.println("❌ Export failed!");
        }
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }
    
    private static void initializeSampleData() {
        // Create sample patient 1
        Patient patient1 = new Patient();
        patient1.setPatientId("PAT001");
        patient1.setFirstName("John");
        patient1.setLastName("Doe");
        patient1.setDateOfBirth(LocalDate.of(1985, 5, 15));
        patient1.setGender("MALE");
        patient1.setBloodGroup("O+");
        patient1.setContactNumber("9876543210");
        patient1.setEmail("john.doe@email.com");
        patient1.setAddress("123 Main Street, City");
        patient1.setEmergencyContact("9876543211");
        patient1.setInsuranceProvider("HealthCare Inc.");
        patient1.setInsuranceNumber("INS123456");
        patient1.addAllergy("Penicillin");
        patient1.addChronicCondition("Hypertension");
        service.registerPatient(patient1);
        
        // Create sample patient 2
        Patient patient2 = new Patient();
        patient2.setPatientId("PAT002");
        patient2.setFirstName("Jane");
        patient2.setLastName("Smith");
        patient2.setDateOfBirth(LocalDate.of(1992, 8, 22));
        patient2.setGender("FEMALE");
        patient2.setBloodGroup("A+");
        patient2.setContactNumber("8765432109");
        patient2.setEmail("jane.smith@email.com");
        patient2.setAddress("456 Oak Avenue, City");
        patient2.setEmergencyContact("8765432108");
        patient2.setInsuranceProvider("MediCare Plus");
        patient2.setInsuranceNumber("INS789012");
        patient2.addAllergy("Dust");
        patient2.addChronicCondition("Asthma");
        service.registerPatient(patient2);
        
        logger.info("Sample data initialized with 2 patients");
    }
}
