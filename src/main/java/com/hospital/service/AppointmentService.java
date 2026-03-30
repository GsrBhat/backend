package com.hospital.service;

import com.hospital.model.Appointment;
import com.hospital.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment bookAppointment(Appointment appointment) {
        // Prevent past booking
        if (appointment.getDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot book an appointment for a past date.");
        }

        // Prevent duplicate booking for same doctor, patient on same day
        Optional<Appointment> existing = appointmentRepository.findByDoctorIdAndPatientIdAndDate(
                appointment.getDoctorId(), appointment.getPatientId(), appointment.getDate());
        if (existing.isPresent()) {
            throw new RuntimeException("You already have an appointment with this doctor on this day.");
        }

        // Check if slot is already taken
        Optional<Appointment> slotOccupied = appointmentRepository.findByDoctorIdAndDateAndTimeSlot(
                appointment.getDoctorId(), appointment.getDate(), appointment.getTimeSlot());
        if (slotOccupied.isPresent()) {
            throw new RuntimeException("This time slot is already booked.");
        }

        // Generate token number (per doctor per day)
        List<Appointment> dailyAppointments = appointmentRepository.findByDoctorIdAndDate(
                appointment.getDoctorId(), appointment.getDate());
        appointment.setTokenNumber(dailyAppointments.size() + 1);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getDoctorAppointments(Long doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndDate(doctorId, date);
    }
}
