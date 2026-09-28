package com.campusfind.campusfind.controller;

import com.campusfind.campusfind.entity.FoundItem;
import com.campusfind.campusfind.service.MatchingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @GetMapping("/lost-reports/{lostReportId}")
    public ResponseEntity<List<FoundItem>> getMatchesForLostReport(@PathVariable Long lostReportId) {
        List<FoundItem> matches = matchingService.findMatchesForLostReport(lostReportId);
        return ResponseEntity.ok(matches);
    }
}
