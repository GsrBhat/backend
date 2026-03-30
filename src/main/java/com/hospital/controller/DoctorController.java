package com.hospital.controller;

import com.hospital.model.User;
import com.hospital.service.DoctorService;
import com.hospital.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AIService aiService;

    @GetMapping
    public List<User> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public User getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    @GetMapping("/top-rated")
    public List<User> getTopRatedDoctors() {
        return doctorService.getTopRatedDoctors();
    }

    @GetMapping("/recommend")
    public ResponseEntity<?> recommendDoctors(@RequestParam String symptom) {
        String specialization = aiService.predictSpecialization(symptom);
        List<User> recommendedDoctors = doctorService.recommendDoctors(specialization);
        
        Map<String, Object> response = new HashMap<>();
        response.put("predictedSpecialization", specialization);
        response.put("doctors", recommendedDoctors);
        
        return ResponseEntity.ok(response);
    }
}
