package net.engineeringdigest.journalApp.mcp.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.List;
import java.util.Collections;

/**
 * Utility helper to format MCP Tool response outputs.
 * Provides builder shortcuts for text, errors, and JSON objects.
 */
public class McpResponse {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()); // Support Java 8 Time types (LocalDateTime)

    /**
     * Build a successful text response.
     *
     * @param text the message content
     * @return MCP CallToolResult
     */
    public static McpSchema.CallToolResult success(String text) {
        List<McpSchema.Content> content = List.of(new McpSchema.TextContent(text));
        return new McpSchema.CallToolResult(
                content,
                false,
                null,
                Collections.emptyMap()
        );
    }

    /**
     * Build a successful JSON serialized response for complex DTO objects.
     *
     * @param data the object to serialize
     * @return MCP CallToolResult
     */
    public static McpSchema.CallToolResult successJson(Object data) {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            return success(json);
        } catch (Exception e) {
            return error("Failed to serialize response: " + e.getMessage());
        }
    }

    /**
     * Build an error response.
     *
     * @param errorMessage the error details
     * @return MCP CallToolResult
     */
    public static McpSchema.CallToolResult error(String errorMessage) {
        List<McpSchema.Content> content = List.of(new McpSchema.TextContent("Error: " + errorMessage));
        return new McpSchema.CallToolResult(
                content,
                true,
                null,
                Collections.emptyMap()
        );
    }
}
