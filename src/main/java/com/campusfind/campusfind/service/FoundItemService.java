package com.campusfind.campusfind.service;

import com.campusfind.campusfind.dto.CreateFoundItemRequest;
import com.campusfind.campusfind.dto.UpdateStatusRequest;
import com.campusfind.campusfind.entity.Category;
import com.campusfind.campusfind.entity.FoundItem;
import com.campusfind.campusfind.entity.ItemStatus;
import com.campusfind.campusfind.entity.Role;
import com.campusfind.campusfind.entity.User;
import com.campusfind.campusfind.exception.BusinessRuleViolationException;
import com.campusfind.campusfind.exception.ResourceNotFoundException;
import com.campusfind.campusfind.exception.UnauthorizedException;
import com.campusfind.campusfind.repository.CategoryRepository;
import com.campusfind.campusfind.repository.FoundItemRepository;
import com.campusfind.campusfind.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoundItemService {

    private final FoundItemRepository foundItemRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public FoundItemService(FoundItemRepository foundItemRepository,
                            UserRepository userRepository,
                            CategoryRepository categoryRepository) {
        this.foundItemRepository = foundItemRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public FoundItem createFoundItem(CreateFoundItemRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        if (user.getRole() != Role.STAFF && user.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only STAFF or ADMIN can report a found item");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        FoundItem item = new FoundItem(
                request.getDescription(),
                request.getLocation(),
                request.getDate(),
                ItemStatus.AVAILABLE,
                category,
                user
        );

        return foundItemRepository.save(item);
    }

    public List<FoundItem> getAllFoundItems() {
        return foundItemRepository.findAll();
    }

    public FoundItem getFoundItemById(Long id) {
        return foundItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Found item not found with id: " + id));
    }

    public FoundItem updateStatus(Long foundItemId, UpdateStatusRequest request) {
        FoundItem foundItem = foundItemRepository.findById(foundItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Found item not found with id: " + foundItemId));

        User actingUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        // Business Rule 2: Only an ADMIN or the STAFF MEMBER who reported/created the found item can change its status.
        boolean isAdmin = actingUser.getRole() == Role.ADMIN;
        boolean isCreatorStaff = actingUser.getId().equals(foundItem.getUser().getId()) && actingUser.getRole() == Role.STAFF;

        if (!isAdmin && !isCreatorStaff) {
            throw new UnauthorizedException("Unauthorized: Only an ADMIN or the STAFF member who created this found item can change its status");
        }

        ItemStatus currentStatus = foundItem.getStatus();
        ItemStatus targetStatus = request.getStatus();

        // Business Rule 1: A found item cannot be marked RETURNED unless it is already CLAIMED.
        // If someone attempts: AVAILABLE -> RETURNED or any other invalid transition, reject immediately.
        if (targetStatus == ItemStatus.RETURNED && currentStatus != ItemStatus.CLAIMED) {
            throw new BusinessRuleViolationException("Invalid status transition: A found item cannot be marked RETURNED unless it is already CLAIMED. Current status: " + currentStatus);
        }

        foundItem.setStatus(targetStatus);
        return foundItemRepository.save(foundItem);
    }
}
