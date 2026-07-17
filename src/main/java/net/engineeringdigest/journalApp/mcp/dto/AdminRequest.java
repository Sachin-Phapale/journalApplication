package net.engineeringdigest.journalApp.mcp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO representing administration requests from MCP tools.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequest {
    private String userName;
}
