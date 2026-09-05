package com.sunrisedental.service;

import com.sunrisedental.dao.AppointmentDAO;
import com.sunrisedental.dao.BillDAO;
import com.sunrisedental.dao.TreatmentDAO;
import com.sunrisedental.model.Appointment;
import com.sunrisedental.model.Bill;
import com.sunrisedental.model.Treatment;

import java.time.LocalDate;

public class BillingService {
    private final AppointmentDAO appointmentDAO;
    private final BillDAO billDAO;
    private final TreatmentDAO treatmentDAO;

    public BillingService() {
        appointmentDAO = new AppointmentDAO();
        billDAO = new BillDAO();
        treatmentDAO = new TreatmentDAO();
    }

    public BillingService(AppointmentDAO appointmentDAO) {
        this.appointmentDAO = appointmentDAO;
        billDAO = new BillDAO();
        treatmentDAO = new TreatmentDAO();
    }

    public double calculateTotal(double treatmentCharge, double consultationFee) {
        if (treatmentCharge < 0 || consultationFee < 0) {
            throw new IllegalArgumentException("Charges cannot be negative.");
        }
        return treatmentCharge + consultationFee;
    }

    public void validateBill(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found.");
        }
        if (!"COMPLETED".equals(appointment.getStatus())) {
            throw new IllegalStateException("Appointment must be completed before billing.");
        }
        if (appointment.getActualTreatmentId() <= 0) {
            throw new IllegalStateException("Treatment must be confirmed before billing.");
        }
    }

    public Bill completeTreatmentAndGenerateBill(int appointmentId,
                                                   int actualTreatmentId,
                                                   double consultationFee) throws Exception {
        if (consultationFee < 0) {
            throw new IllegalArgumentException("Consultation fee cannot be negative.");
        }

        Appointment appointment = appointmentDAO.findById(appointmentId);

        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found.");
        }
        if ("CANCELLED".equals(appointment.getStatus())) {
            throw new IllegalStateException("Cancelled appointments cannot be completed.");
        }
        if ("COMPLETED".equals(appointment.getStatus())) {
            Bill existing = billDAO.findByAppointmentId(appointmentId);
            if (existing != null) {
                return existing;
            }
            throw new IllegalStateException("Treatment is already completed.");
        }

        Treatment treatment = treatmentDAO.findById(actualTreatmentId);
        if (treatment == null || !treatment.isActive()) {
            throw new IllegalArgumentException("Please select a valid active treatment.");
        }

        double total = calculateTotal(treatment.getCharge(), consultationFee);
        appointmentDAO.complete(appointmentId, actualTreatmentId);

        String billNumber = billDAO.generateBillNumber(LocalDate.now().toString());
        return billDAO.create(appointmentId, billNumber, treatment.getCharge(), consultationFee);
    }

    public Bill findByAppointmentId(int appointmentId) throws Exception {
        return billDAO.findByAppointmentId(appointmentId);
    }

    public java.util.List<Bill> findRecentBills(int limit) throws Exception {
        return billDAO.findRecent(limit);
    }
}
