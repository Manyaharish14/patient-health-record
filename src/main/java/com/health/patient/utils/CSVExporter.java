package com.health.patient.utils;

import com.health.patient.models.Patient;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

public class CSVExporter {
    private static final Logger logger = LoggerFactory.getLogger(CSVExporter.class);
    
    public static void exportPatientToCSV(Patient patient, String filename) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filename))) {
            String[] header = {
                "Patient ID", "First Name", "Last Name", "Age", "Gender", 
                "Blood Group", "Contact", "Email", "Address", 
                "Emergency Contact", "Allergies", "Chronic Conditions",
                "Insurance Provider", "Insurance Number"
            };
            writer.writeNext(header);
            
            String[] data = {
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                String.valueOf(patient.getAge()),
                patient.getGender(),
                patient.getBloodGroup(),
                patient.getContactNumber(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getEmergencyContact(),
                String.join(";", patient.getAllergies()),
                String.join(";", patient.getChronicConditions()),
                patient.getInsuranceProvider(),
                patient.getInsuranceNumber()
            };
            writer.writeNext(data);
            
            logger.info("Patient data exported to CSV: {}", filename);
        }
    }
}
