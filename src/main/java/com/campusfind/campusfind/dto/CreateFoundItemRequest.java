package com.campusfind.campusfind.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class CreateFoundItemRequest {

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Location cannot be blank")
    private String location;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    private Long categoryId;

    @NotNull(message = "User ID (Staff) is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    public CreateFoundItemRequest() {
    }

    public CreateFoundItemRequest(String description, String location, LocalDate date, Long categoryId, Long userId) {
        this.description = description;
        this.location = location;
        this.date = date;
        this.categoryId = categoryId;
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
