package com.health.patient;

import com.health.patient.models.*;
import com.health.patient.services.PatientHealthService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AppTest {
    
    private PatientHealthService service;
    private Patient testPatient;
    
    @Before
    public void setUp() {
        service = new PatientHealthService();
        
        testPatient = new Patient();
        testPatient.setPatientId("TEST001");
        testPatient.setFirstName("Test");
        testPatient.setLastName("User");
        testPatient.setDateOfBirth(LocalDate.of(1995, 1, 1));
        testPatient.setGender("MALE");
        testPatient.setBloodGroup("O+");
        testPatient.setContactNumber("1234567890");
        testPatient.setEmail("test@example.com");
        testPatient.setAddress("Test Address");
        testPatient.setEmergencyContact("0987654321");
        
        service.registerPatient(testPatient);
    }
    
    @Test
    public void testRegisterPatient() {
        Patient newPatient = new Patient();
        newPatient.setPatientId("TEST002");
        newPatient.setFirstName("Test2");
        newPatient.setLastName("User2");
        newPatient.setDateOfBirth(LocalDate.of(1996, 2, 2));
        newPatient.setGender("FEMALE");
        newPatient.setBloodGroup("A+");
        newPatient.setContactNumber("1111111111");
        newPatient.setEmail("test2@example.com");
        
        boolean result = service.registerPatient(newPatient);
        assertTrue(result);
        
        Patient retrieved = service.getPatientById("TEST002");
        assertNotNull(retrieved);
        assertEquals("Test2", retrieved.getFirstName());
    }
    
    @Test
    public void testRegisterDuplicatePatient() {
        Patient duplicate = testPatient;
        boolean result = service.registerPatient(duplicate);
        assertFalse(result);
    }
    
    @Test
    public void testGetPatientById() {
        Patient retrieved = service.getPatientById("TEST001");
        assertNotNull(retrieved);
        assertEquals("Test User", retrieved.getFullName());
        
        Patient notFound = service.getPatientById("INVALID");
        assertNull(notFound);
    }
    
    @Test
    public void testUpdatePatient() {
        testPatient.setContactNumber("9999999999");
        boolean result = service.updatePatient(testPatient);
        assertTrue(result);
        
        Patient updated = service.getPatientById("TEST001");
        assertEquals("9999999999", updated.getContactNumber());
    }
    
    @Test
    public void testDeletePatient() {
        boolean result = service.deletePatient("TEST001");
        assertTrue(result);
        
        Patient deleted = service.getPatientById("TEST001");
        assertNull(deleted);
    }
    
    @Test
    public void testAddMedicalRecord() {
        MedicalRecord record = new MedicalRecord();
        record.setPatientId("TEST001");
        record.setDoctorName("Dr. Smith");
        record.setDepartment("Cardiology");
        record.setDiagnosis("Hypertension");
        record.setSymptoms("High blood pressure");
        record.setPrescribedMedications("Lisinopril");
        
        boolean result = service.addMedicalRecord(record);
        assertTrue(result);
        
        List<MedicalRecord> records = service.getPatientMedicalRecords("TEST001");
        assertEquals(1, records.size());
        assertEquals("Hypertension", records.get(0).getDiagnosis());
    }
    
    @Test
    public void testAddMedicalRecordForNonExistentPatient() {
        MedicalRecord record = new MedicalRecord();
        record.setPatientId("INVALID");
        record.setDoctorName("Dr. Smith");
        record.setDiagnosis("Flu");
        
        boolean result = service.addMedicalRecord(record);
        assertFalse(result);
    }
    
    @Test
    public void testScheduleAppointment() {
        Appointment appointment = new Appointment();
        appointment.setPatientId("TEST001");
        appointment.setDoctorName("Dr. Jones");
        appointment.setDepartment("General Medicine");
        appointment.setAppointmentDateTime(LocalDateTime.now().plusDays(5));
        appointment.setReason("Regular checkup");
        
        boolean result = service.scheduleAppointment(appointment);
        assertTrue(result);
        
        List<Appointment> appointments = service.getPatientAppointments("TEST001");
        assertEquals(1, appointments.size());
    }
    
    @Test
    public void testGetUpcomingAppointments() {
        Appointment pastAppointment = new Appointment();
        pastAppointment.setPatientId("TEST001");
        pastAppointment.setAppointmentDateTime(LocalDateTime.now().minusDays(1));
        pastAppointment.setStatus("SCHEDULED");
        service.scheduleAppointment(pastAppointment);
        
        Appointment futureAppointment = new Appointment();
        futureAppointment.setPatientId("TEST001");
        futureAppointment.setAppointmentDateTime(LocalDateTime.now().plusDays(5));
        futureAppointment.setStatus("SCHEDULED");
        service.scheduleAppointment(futureAppointment);
        
        List<Appointment> upcoming = service.getUpcomingAppointments("TEST001");
        assertEquals(1, upcoming.size());
        assertTrue(upcoming.get(0).getAppointmentDateTime().isAfter(LocalDateTime.now()));
    }
    
    @Test
    public void testCancelAppointment() {
        Appointment appointment = new Appointment();
        appointment.setPatientId("TEST001");
        appointment.setDoctorName("Dr. Brown");
        appointment.setAppointmentDateTime(LocalDateTime.now().plusDays(3));
        service.scheduleAppointment(appointment);
        
        List<Appointment> appointments = service.getPatientAppointments("TEST001");
        String appointmentId = appointments.get(0).getAppointmentId();
        
        boolean result = service.cancelAppointment("TEST001", appointmentId);
        assertTrue(result);
        
        Appointment cancelled = service.getPatientAppointments("TEST001").get(0);
        assertEquals("CANCELLED", cancelled.getStatus());
    }
    
    @Test
    public void testSearchPatients() {
        List<Patient> results = service.searchPatients("Test");
        assertEquals(1, results.size());
        
        results = service.searchPatients("TEST001");
        assertEquals(1, results.size());
        
        results = service.searchPatients("1234567890");
        assertEquals(1, results.size());
        
        results = service.searchPatients("Nonexistent");
        assertEquals(0, results.size());
    }
    
    @Test
public void testGetPatientHealthSummary() {
    MedicalRecord record = new MedicalRecord();
    record.setPatientId("TEST001");
    record.setDiagnosis("Cold");
    record.setDoctorName("Dr. White");
    service.addMedicalRecord(record);
    
    var summary = service.getPatientHealthSummary("TEST001");
    assertNotNull(summary);
    // Fix: Change from assertEquals(1L, ...) to this:
    assertEquals(1, ((Number) summary.get("totalVisits")).intValue());
}
    
    @Test
    public void testGetSystemStatistics() {
        var stats = service.getSystemStatistics();
        assertNotNull(stats);
        assertTrue((Integer) stats.get("totalPatients") >= 1);
    }
    
    @Test
    public void testValidatePatient() {
        Patient invalidPatient = new Patient();
        invalidPatient.setPatientId("");
        invalidPatient.setFirstName("");
        
        assertFalse(service.isValidPatient(invalidPatient));
        assertTrue(service.isValidPatient(testPatient));
    }
    
    @Test
    public void testPatientAgeCalculation() {
        testPatient.setDateOfBirth(LocalDate.now().minusYears(25));
        assertEquals(25, testPatient.getAge());
        
        testPatient.setDateOfBirth(null);
        assertEquals(0, testPatient.getAge());
    }
    
    @Test
    public void testGeneratePatientId() {
        String id1 = service.generatePatientId();
        String id2 = service.generatePatientId();
        
        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1, id2);
        assertTrue(id1.startsWith("PAT"));
        assertTrue(id2.startsWith("PAT"));
    }
    
    @Test
    public void testGetMedicalRecordsByDateRange() {
        MedicalRecord record1 = new MedicalRecord();
        record1.setPatientId("TEST001");
        record1.setRecordDate(LocalDateTime.now().minusDays(10));
        service.addMedicalRecord(record1);
        
        MedicalRecord record2 = new MedicalRecord();
        record2.setPatientId("TEST001");
        record2.setRecordDate(LocalDateTime.now().minusDays(5));
        service.addMedicalRecord(record2);
        
        MedicalRecord record3 = new MedicalRecord();
        record3.setPatientId("TEST001");
        record3.setRecordDate(LocalDateTime.now().plusDays(1));
        service.addMedicalRecord(record3);
        
        List<MedicalRecord> range = service.getMedicalRecordsByDateRange(
            "TEST001",
            LocalDateTime.now().minusDays(7),
            LocalDateTime.now()
        );
        
        assertEquals(1, range.size());
    }
}
