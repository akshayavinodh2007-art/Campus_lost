package com.campusfind.campusfind.service;

import com.campusfind.campusfind.entity.FoundItem;
import com.campusfind.campusfind.entity.ItemStatus;
import com.campusfind.campusfind.entity.LostReport;
import com.campusfind.campusfind.exception.ResourceNotFoundException;
import com.campusfind.campusfind.repository.FoundItemRepository;
import com.campusfind.campusfind.repository.LostReportRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchingService {

    private final LostReportRepository lostReportRepository;
    private final FoundItemRepository foundItemRepository;

    public MatchingService(LostReportRepository lostReportRepository,
                           FoundItemRepository foundItemRepository) {
        this.lostReportRepository = lostReportRepository;
        this.foundItemRepository = foundItemRepository;
    }

    public List<FoundItem> findMatchesForLostReport(Long lostReportId) {
        LostReport lostReport = lostReportRepository.findById(lostReportId)
                .orElseThrow(() -> new ResourceNotFoundException("Lost report not found with id: " + lostReportId));

        // Get available found items in the same category
        List<FoundItem> candidateItems = foundItemRepository.findByCategoryAndStatus(
                lostReport.getCategory(),
                ItemStatus.AVAILABLE
        );

        List<FoundItem> matches = new ArrayList<>();
        Set<String> lostKeywords = extractKeywords(lostReport.getDescription());
        String lostLocation = lostReport.getLocation() != null ? lostReport.getLocation().trim().toLowerCase() : "";

        for (FoundItem foundItem : candidateItems) {
            String foundLocation = foundItem.getLocation() != null ? foundItem.getLocation().trim().toLowerCase() : "";

            // Check location match (exact or substring/contains)
            boolean locationMatches = !lostLocation.isEmpty() && !foundLocation.isEmpty() &&
                    (lostLocation.contains(foundLocation) || foundLocation.contains(lostLocation));

            // Check description keyword overlap
            Set<String> foundKeywords = extractKeywords(foundItem.getDescription());
            boolean keywordMatches = false;
            for (String kw : lostKeywords) {
                if (foundKeywords.contains(kw)) {
                    keywordMatches = true;
                    break;
                }
            }

            // If same category and (location matches or description keywords match)
            if (locationMatches || keywordMatches) {
                matches.add(foundItem);
            }
        }

        // If candidateItems exist in category and no specific keyword/location match was found,
        // candidateItems in the same category are also eligible candidate matches
        if (matches.isEmpty() && !candidateItems.isEmpty()) {
            return candidateItems;
        }

        return matches;
    }

    private Set<String> extractKeywords(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptySet();
        }
        Set<String> stopWords = Set.of(
                "a", "an", "the", "in", "on", "at", "by", "for", "with", "about",
                "against", "between", "into", "through", "during", "before", "after",
                "above", "below", "to", "from", "up", "down", "out", "off",
                "over", "under", "again", "further", "then", "once", "here", "there",
                "when", "where", "why", "how", "all", "any", "both", "each", "few",
                "more", "most", "other", "some", "such", "no", "nor", "not", "only",
                "own", "same", "so", "than", "too", "very", "s", "t", "can", "will",
                "just", "don", "should", "now", "i", "my", "me", "lost", "found", "item", "color", "is", "was"
        );

        String cleaned = text.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", " ");
        String[] tokens = cleaned.split("\\s+");
        Set<String> keywords = new HashSet<>();
        for (String token : tokens) {
            if (token.length() > 2 && !stopWords.contains(token)) {
                keywords.add(token);
            }
        }
        return keywords;
    }
}
