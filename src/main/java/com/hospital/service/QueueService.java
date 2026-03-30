package com.hospital.service;

import com.hospital.model.Appointment;
import com.hospital.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueueService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Map<String, Object> getQueueStatus(Long doctorId) {
        LocalDate today = LocalDate.now();
        List<Appointment> dailyAppointments = appointmentRepository.findByDoctorIdAndDate(doctorId, today);

        // Simple current token logic (could be more complex if we had a "mark as checked" feature)
        // For now, let's assume the first appointment of the day is current
        int total = dailyAppointments.size();
        int currentToken = total > 0 ? 1 : 0; // Simplified for demo
        int nextToken = total > 1 ? 2 : 0;

        Map<String, Object> status = new HashMap<>();
        status.put("doctorId", doctorId);
        status.put("date", today);
        status.put("totalTokens", total);
        status.put("currentToken", currentToken);
        status.put("nextToken", nextToken);
        status.put("appointments", dailyAppointments);

        return status;
    }
}
