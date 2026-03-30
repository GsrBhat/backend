package com.hospital.repository;

import com.hospital.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorIdAndDate(Long doctorId, LocalDate date);
    Optional<Appointment> findByDoctorIdAndPatientIdAndDate(Long doctorId, Long patientId, LocalDate date);
    Optional<Appointment> findByDoctorIdAndDateAndTimeSlot(Long doctorId, LocalDate date, String timeSlot);
}
