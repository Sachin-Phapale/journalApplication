package net.engineeringdigest.journalApp.mcp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO representing search queries for journal entries from MCP tools.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private String title;
    private String tag;
    private String sentiment;
}
