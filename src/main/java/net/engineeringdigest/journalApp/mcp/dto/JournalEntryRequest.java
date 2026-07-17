package net.engineeringdigest.journalApp.mcp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO representing journal entry requests from MCP tools.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryRequest {
    private String id;
    private String title;
    private String content;
    private String sentiment;
}
