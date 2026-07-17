package net.engineeringdigest.journalApp.mcp.exception;

/**
 * Custom exception representing errors during MCP tool execution.
 * Wraps validation, security, or business logic errors so they can be
 * formatted cleanly and returned to the LLM client.
 */
public class McpToolException extends RuntimeException {

    public McpToolException(String message) {
        super(message);
    }

    public McpToolException(String message, Throwable cause) {
        super(message, cause);
    }
}
