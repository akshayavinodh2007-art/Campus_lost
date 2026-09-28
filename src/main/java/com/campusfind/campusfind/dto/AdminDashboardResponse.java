package com.campusfind.campusfind.dto;

public class AdminDashboardResponse {

    private long pendingLostReports;
    private long matchedItems;
    private long returnedItems;

    public AdminDashboardResponse() {
    }

    public AdminDashboardResponse(long pendingLostReports, long matchedItems, long returnedItems) {
        this.pendingLostReports = pendingLostReports;
        this.matchedItems = matchedItems;
        this.returnedItems = returnedItems;
    }

    public long getPendingLostReports() {
        return pendingLostReports;
    }

    public void setPendingLostReports(long pendingLostReports) {
        this.pendingLostReports = pendingLostReports;
    }

    public long getMatchedItems() {
        return matchedItems;
    }

    public void setMatchedItems(long matchedItems) {
        this.matchedItems = matchedItems;
    }

    public long getReturnedItems() {
        return returnedItems;
    }

    public void setReturnedItems(long returnedItems) {
        this.returnedItems = returnedItems;
    }
}
