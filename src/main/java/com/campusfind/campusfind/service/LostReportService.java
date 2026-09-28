package com.campusfind.campusfind.service;

import com.campusfind.campusfind.dto.CreateLostReportRequest;
import com.campusfind.campusfind.entity.Category;
import com.campusfind.campusfind.entity.LostReport;
import com.campusfind.campusfind.entity.User;
import com.campusfind.campusfind.exception.ResourceNotFoundException;
import com.campusfind.campusfind.repository.CategoryRepository;
import com.campusfind.campusfind.repository.LostReportRepository;
import com.campusfind.campusfind.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LostReportService {

    private final LostReportRepository lostReportRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public LostReportService(LostReportRepository lostReportRepository,
                             UserRepository userRepository,
                             CategoryRepository categoryRepository) {
        this.lostReportRepository = lostReportRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public LostReport createLostReport(CreateLostReportRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        LostReport report = new LostReport(
                request.getDescription(),
                request.getLocation(),
                request.getDate(),
                category,
                user
        );

        return lostReportRepository.save(report);
    }

    public List<LostReport> getAllLostReports() {
        return lostReportRepository.findAll();
    }

    public LostReport getLostReportById(Long id) {
        return lostReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lost report not found with id: " + id));
    }
}
