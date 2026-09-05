package com.sunrisedental.service;

import com.sunrisedental.dao.AppointmentDAO;
import com.sunrisedental.model.Appointment;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {
    private AppointmentDAO dao;
    private AppointmentService service;
    @BeforeEach void setup() {
        dao=mock(AppointmentDAO.class);
        service=new AppointmentService(dao);
    }
    private Appointment valid() {
        Appointment a=new Appointment();
        a.setPatientName("Nimal");
        a.setPatientEmail("nimal@example.com");
        a.setAddress("Colombo");
        a.setContactNumber("0771234567");
        a.setDentistId(1);
        a.setRequestedTreatmentId(1);
        a.setAppointmentDate("2099-09-10");
        a.setAppointmentTime("10:00");
        return a;
    }
    @Test void rejectDoubleBooking()throws Exception {
        Appointment a=valid();
        when(dao.isAvailable(1,"2099-09-10","10:00",0)).thenReturn(false);
        assertThrows(IllegalArgumentException.class,()->service.create(a));
    }
    @Test void allowAvailableSlot()throws Exception {
        Appointment a=valid();
        when(dao.isAvailable(1,"2099-09-10","10:00",0)).thenReturn(true);
        when(dao.save(a)).thenReturn(a);
        assertNotNull(service.create(a));
        verify(dao).save(a);
    }
    @Test void rejectInvalidEmail()throws Exception {
        Appointment a=valid();
        a.setPatientEmail("bad");
        assertThrows(IllegalArgumentException.class,()->service.create(a));
        verifyNoInteractions(dao);
    }
    @Test void rejectMissingDentist()throws Exception {
        Appointment a=valid();
        a.setDentistId(0);
        assertThrows(IllegalArgumentException.class,()->service.create(a));
    }
    @Test void rejectMissingTreatment()throws Exception {
        Appointment a=valid();
        a.setRequestedTreatmentId(0);
        assertThrows(IllegalArgumentException.class,()->service.create(a));
    }
    @Test void rejectPastDate()throws Exception {
        Appointment a=valid();
        a.setAppointmentDate("2020-01-01");
        assertThrows(IllegalArgumentException.class,()->service.create(a));
    }
}
