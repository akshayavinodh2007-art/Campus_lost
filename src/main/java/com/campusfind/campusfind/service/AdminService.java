package com.campusfind.campusfind.service;

import com.campusfind.campusfind.dto.AdminDashboardResponse;
import com.campusfind.campusfind.entity.ItemStatus;
import com.campusfind.campusfind.repository.FoundItemRepository;
import com.campusfind.campusfind.repository.LostReportRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final LostReportRepository lostReportRepository;
    private final FoundItemRepository foundItemRepository;

    public AdminService(LostReportRepository lostReportRepository,
                        FoundItemRepository foundItemRepository) {
        this.lostReportRepository = lostReportRepository;
        this.foundItemRepository = foundItemRepository;
    }

    public AdminDashboardResponse getDashboardData() {
        long pendingLostReports = lostReportRepository.count();
        long matchedItems = foundItemRepository.countByStatus(ItemStatus.CLAIMED);
        long returnedItems = foundItemRepository.countByStatus(ItemStatus.RETURNED);

        return new AdminDashboardResponse(pendingLostReports, matchedItems, returnedItems);
    }
}
