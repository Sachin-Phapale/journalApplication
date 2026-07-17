package net.engineeringdigest.journalApp.mcp.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * Output DTO representing a safe user profile.
 */
@Data
@Builder
public class UserProfileResponse {
    private String userName;
    private String email;
    private boolean sentimentAnalysis;
    private List<String> roles;
    private int totalJournalEntries;
}
