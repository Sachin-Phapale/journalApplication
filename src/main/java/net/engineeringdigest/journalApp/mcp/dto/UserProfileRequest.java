package net.engineeringdigest.journalApp.mcp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO representing user profile updates from MCP tools.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequest {
    private String email;
    private String password;
    private Boolean sentimentAnalysis;
}
