package net.engineeringdigest.journalApp.mcp.tools;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.mcp.dto.AdminStatsResponse;
import net.engineeringdigest.journalApp.mcp.exception.McpToolException;
import net.engineeringdigest.journalApp.mcp.security.McpSecurityContext;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MCP Tools for Admin Operations.
 * Enforces role-based security access checks before processing commands.
 */
@Component
@Slf4j
public class AdminTools {

    private final UserService userService;
    private final JournalEntryService journalEntryService;
    private final McpSecurityContext mcpSecurityContext;

    /**
     * Constructor injection.
     */
    public AdminTools(UserService userService,
                      JournalEntryService journalEntryService,
                      McpSecurityContext mcpSecurityContext) {
        this.userService = userService;
        this.journalEntryService = journalEntryService;
        this.mcpSecurityContext = mcpSecurityContext;
    }

    /**
     * Verify that the current context user has the ADMIN role.
     */
    private void checkAdminAccess() {
        String username = mcpSecurityContext.getCurrentUsername();
        User currentUser = userService.findByUserName(username);
        if (currentUser == null) {
            throw new McpToolException("Context user not found: " + username);
        }
        if (currentUser.getRoles() == null || !currentUser.getRoles().contains("ADMIN")) {
            throw new McpToolException("Access Denied: Admin privileges required for: " + username);
        }
    }

    /**
     * Tool: Retrieve all users in the system.
     *
     * @return List of Users
     */
    public List<User> getAllUsers() {
        checkAdminAccess();
        log.info("Admin tool: retrieving all users");
        return userService.getAll();
    }

    /**
     * Tool: Delete a user by username.
     *
     * @param userName username of the user to delete
     * @return success statement
     */
    public String deleteUser(String userName) {
        checkAdminAccess();
        if (userName == null || userName.trim().isEmpty()) {
            throw new McpToolException("UserName parameter is required");
        }

        log.info("Admin tool: deleting user {}", userName);
        User targetUser = userService.findByUserName(userName);
        if (targetUser == null) {
            throw new McpToolException("User not found: " + userName);
        }

        userService.deleteById(targetUser.getId());
        return "User '" + userName + "' deleted successfully";
    }

    /**
     * Tool: Get system statistics dashboard.
     *
     * @return AdminStatsResponse containing counts and calculations
     */
    public AdminStatsResponse getStatistics() {
        checkAdminAccess();
        log.info("Admin tool: calculating system statistics");

        List<User> users = userService.getAll();
        long totalUsers = users.size();
        long totalEntries = journalEntryService.getAll().size();

        double average = totalUsers > 0 ? (double) totalEntries / totalUsers : 0.0;
        average = Math.round(average * 100.0) / 100.0; // round to 2 decimals

        long sentimentAnalysisEnabledUsers = users.stream()
                .filter(User::isSentimentAnalysis)
                .count();

        return AdminStatsResponse.builder()
                .totalUsers(totalUsers)
                .totalJournalEntries(totalEntries)
                .averageEntriesPerUser(average)
                .activeUsersWithSentimentAnalysis(sentimentAnalysisEnabledUsers)
                .build();
    }
}
