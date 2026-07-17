package net.engineeringdigest.journalApp.mcp.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Output DTO for administrative dashboard statistics.
 */
@Data
@Builder
public class AdminStatsResponse {
    private long totalUsers;
    private long totalJournalEntries;
    private double averageEntriesPerUser;
    private long activeUsersWithSentimentAnalysis;
}
