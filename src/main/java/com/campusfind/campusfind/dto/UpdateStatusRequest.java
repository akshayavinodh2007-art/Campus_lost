package com.campusfind.campusfind.dto;

import com.campusfind.campusfind.entity.ItemStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateStatusRequest {

    @NotNull(message = "User ID performing the update is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    @NotNull(message = "New status is required (AVAILABLE, CLAIMED, RETURNED)")
    private ItemStatus status;

    public UpdateStatusRequest() {
    }

    public UpdateStatusRequest(Long userId, ItemStatus status) {
        this.userId = userId;
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }
}
