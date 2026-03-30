package com.hospital.service;

import com.hospital.model.Review;
import com.hospital.model.User;
import com.hospital.repository.ReviewRepository;
import com.hospital.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    public Review addReview(Review review) {
        Review saved = reviewRepository.save(review);
        updateDoctorAverageRating(review.getDoctorId());
        return saved;
    }

    public List<Review> getDoctorReviews(Long doctorId) {
        return reviewRepository.findByDoctorId(doctorId);
    }

    private void updateDoctorAverageRating(Long doctorId) {
        List<Review> reviews = reviewRepository.findByDoctorId(doctorId);
        if (reviews.isEmpty()) return;

        double sum = reviews.stream().mapToInt(Review::getRating).sum();
        double average = sum / reviews.size();

        User doctor = userRepository.findById(doctorId).orElseThrow();
        doctor.setRating(average);
        userRepository.save(doctor);
    }
}
