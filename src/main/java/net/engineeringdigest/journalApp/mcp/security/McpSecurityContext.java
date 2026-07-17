package net.engineeringdigest.journalApp.mcp.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Helper component to resolve the authenticated user in MCP tools.
 * Supports both SSE transport (integrated with JWT Spring Security)
 * and STDIO transport (using thread-local override or default-user property fallback).
 */
@Component
public class McpSecurityContext {

    private static final ThreadLocal<String> threadLocalUser = new ThreadLocal<>();

    @Value("${mcp.security.default-user:admin}")
    private String defaultUser;

    /**
     * Set the username for the current thread execution.
     * Useful for programmatic authentication during tool invocation.
     *
     * @param username the username to set
     */
    public static void setThreadLocalUser(String username) {
        threadLocalUser.set(username);
    }

    /**
     * Clear the thread-local username.
     */
    public static void clear() {
        threadLocalUser.remove();
    }

    /**
     * Resolve the current username.
     *
     * @return the resolved username
     */
    public String getCurrentUsername() {
        // 1. Check thread-local user (set specifically for this tool execution)
        String user = threadLocalUser.get();
        if (user != null && !user.trim().isEmpty()) {
            return user;
        }

        // 2. Check standard Spring Security Context (populated by JWT Filter in HTTP/SSE requests)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName();
        }

        // 3. Fallback to default configured user
        return defaultUser;
    }
}
