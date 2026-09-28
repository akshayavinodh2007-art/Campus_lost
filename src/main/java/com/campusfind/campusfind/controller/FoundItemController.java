package com.campusfind.campusfind.controller;

import com.campusfind.campusfind.dto.CreateFoundItemRequest;
import com.campusfind.campusfind.dto.UpdateStatusRequest;
import com.campusfind.campusfind.entity.FoundItem;
import com.campusfind.campusfind.service.FoundItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/found-items")
public class FoundItemController {

    private final FoundItemService foundItemService;

    public FoundItemController(FoundItemService foundItemService) {
        this.foundItemService = foundItemService;
    }

    @PostMapping
    public ResponseEntity<FoundItem> createFoundItem(@Valid @RequestBody CreateFoundItemRequest request) {
        FoundItem createdItem = foundItemService.createFoundItem(request);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FoundItem>> getAllFoundItems() {
        return ResponseEntity.ok(foundItemService.getAllFoundItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoundItem> getFoundItemById(@PathVariable Long id) {
        return ResponseEntity.ok(foundItemService.getFoundItemById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<FoundItem> updateStatus(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateStatusRequest request) {
        FoundItem updatedItem = foundItemService.updateStatus(id, request);
        return ResponseEntity.ok(updatedItem);
    }
}
