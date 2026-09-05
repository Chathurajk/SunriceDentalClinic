package com.sunrisedental.service;

import com.sunrisedental.model.Appointment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BillingServiceTest {
    @Test void total() {
        assertEquals(10000,new BillingService().calculateTotal(8500,1500));
    }
    @Test void negativeCharge() {
        assertThrows(IllegalArgumentException.class,()->new BillingService().calculateTotal(-1,1500));
    }
    @Test void bookedCannotBill() {
        Appointment a=new Appointment();
        a.setStatus("BOOKED");
        a.setActualTreatmentId(1);
        assertThrows(IllegalStateException.class,()->new BillingService().validateBill(a));
    }
    @Test void noTreatmentCannotBill() {
        Appointment a=new Appointment();
        a.setStatus("COMPLETED");
        assertThrows(IllegalStateException.class,()->new BillingService().validateBill(a));
    }
}
