package com.campusfind.campusfind.config;

import com.campusfind.campusfind.dto.CreateFoundItemRequest;
import com.campusfind.campusfind.dto.CreateLostReportRequest;
import com.campusfind.campusfind.dto.RegisterRequest;
import com.campusfind.campusfind.entity.Category;
import com.campusfind.campusfind.entity.Role;
import com.campusfind.campusfind.service.CategoryService;
import com.campusfind.campusfind.service.FoundItemService;
import com.campusfind.campusfind.service.LostReportService;
import com.campusfind.campusfind.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserService userService,
                                      CategoryService categoryService,
                                      LostReportService lostReportService,
                                      FoundItemService foundItemService) {
        return args -> {
            try {
                // Seed Users
                var student = userService.registerUser(new RegisterRequest("alex_student", "pass123", Role.STUDENT));
                var staff = userService.registerUser(new RegisterRequest("sarah_staff", "pass123", Role.STAFF));
                var admin = userService.registerUser(new RegisterRequest("admin_dean", "pass123", Role.ADMIN));

                // Seed Categories
                var electronics = categoryService.createCategory(new Category("Electronics"));
                var documents = categoryService.createCategory(new Category("IDs & Documents"));
                var accessories = categoryService.createCategory(new Category("Accessories"));

                // Seed sample Lost Reports
                lostReportService.createLostReport(new CreateLostReportRequest(
                        "Lost Black HP Laptop with stickers",
                        "Main Library Room 204",
                        LocalDate.now(),
                        electronics.getId(),
                        student.getId()
                ));

                lostReportService.createLostReport(new CreateLostReportRequest(
                        "Student ID card and library card in red holder",
                        "Campus Cafeteria",
                        LocalDate.now(),
                        documents.getId(),
                        student.getId()
                ));

                // Seed sample Found Items
                foundItemService.createFoundItem(new CreateFoundItemRequest(
                        "Found Black HP Laptop near study tables",
                        "Main Library Room 204",
                        LocalDate.now(),
                        electronics.getId(),
                        staff.getId()
                ));

                foundItemService.createFoundItem(new CreateFoundItemRequest(
                        "Found Blue Sports Water Bottle",
                        "Basketball Court",
                        LocalDate.now(),
                        accessories.getId(),
                        staff.getId()
                ));
            } catch (Exception ignored) {
                // Ignore if already seeded
            }
        };
    }
}
