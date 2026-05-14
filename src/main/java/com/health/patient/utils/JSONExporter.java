package com.health.patient.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.health.patient.models.MedicalRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JSONExporter {
    private static final Logger logger = LoggerFactory.getLogger(JSONExporter.class);
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);
    
    public static void exportMedicalRecordsToJSON(List<MedicalRecord> records, String filename) 
            throws IOException {
        mapper.writeValue(new File(filename), records);
        logger.info("Medical records exported to JSON: {}", filename);
    }
}
