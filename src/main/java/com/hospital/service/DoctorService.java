package com.hospital.service;

import com.hospital.model.User;
import com.hospital.model.Role;
import com.hospital.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllDoctors() {
        return userRepository.findByRole(Role.DOCTOR);
    }

    public User getDoctorById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    public List<User> getTopRatedDoctors() {
        return userRepository.findByRole(Role.DOCTOR).stream()
            .sorted((d1, d2) -> d2.getRating().compareTo(d1.getRating()))
            .limit(5)
            .collect(Collectors.toList());
    }

    public List<User> recommendDoctors(String specialization) {
        return userRepository.findByRoleAndSpecialization(Role.DOCTOR, specialization).stream()
            .sorted((d1, d2) -> d2.getRating().compareTo(d1.getRating()))
            .collect(Collectors.toList());
    }
}
