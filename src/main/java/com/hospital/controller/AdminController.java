package com.hospital.controller;

import com.hospital.model.User;
import com.hospital.model.Role;
import com.hospital.repository.UserRepository;
import com.hospital.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/dashboard")
    public Map<String, Object> getAdminDashboard() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalDoctors", userRepository.findByRole(Role.DOCTOR).size());
        stats.put("totalPatients", userRepository.findByRole(Role.PATIENT).size());
        stats.put("totalAppointments", appointmentRepository.count());
        
        List<User> doctors = userRepository.findByRole(Role.DOCTOR);
        stats.put("doctors", doctors);
        
        return stats;
    }
}
