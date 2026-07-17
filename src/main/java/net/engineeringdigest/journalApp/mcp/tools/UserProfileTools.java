package net.engineeringdigest.journalApp.mcp.tools;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.mcp.dto.UserProfileResponse;
import net.engineeringdigest.journalApp.mcp.exception.McpToolException;
import net.engineeringdigest.journalApp.mcp.security.McpSecurityContext;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * MCP Tools for User Profile Operations.
 */
@Component
@Slf4j
public class UserProfileTools {

    private final UserService userService;
    private final McpSecurityContext mcpSecurityContext;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Constructor injection.
     */
    public UserProfileTools(UserService userService, McpSecurityContext mcpSecurityContext) {
        this.userService = userService;
        this.mcpSecurityContext = mcpSecurityContext;
    }

    /**
     * Tool: Retrieve the profile of the currently authenticated user.
     *
     * @return UserProfileResponse containing safe profile details
     */
    public UserProfileResponse getUserProfile() {
        String username = mcpSecurityContext.getCurrentUsername();
        log.info("Fetching profile for user: {}", username);

        User user = userService.findByUserName(username);
        if (user == null) {
            throw new McpToolException("Authenticated user context not found in repository: " + username);
        }

        return mapToProfileResponse(user);
    }

    /**
     * Tool: Update user profile settings.
     *
     * @param email             the new email address (optional)
     * @param password          the new password (optional, will be hashed)
     * @param sentimentAnalysis the new setting for sentiment analysis (optional)
     * @return UserProfileResponse containing updated profile details
     */
    public UserProfileResponse updateUserProfile(String email, String password, Boolean sentimentAnalysis) {
        String username = mcpSecurityContext.getCurrentUsername();
        log.info("Updating profile details for user: {}", username);

        User user = userService.findByUserName(username);
        if (user == null) {
            throw new McpToolException("Authenticated user context not found: " + username);
        }

        boolean updated = false;

        if (email != null && !email.trim().isEmpty()) {
            user.setEmail(email);
            updated = true;
        }

        if (sentimentAnalysis != null) {
            user.setSentimentAnalysis(sentimentAnalysis);
            updated = true;
        }

        if (password != null && !password.trim().isEmpty()) {
            // Hash the password manually to prevent resetting user roles (which occurs inside userService.saveNewUser)
            user.setPassword(passwordEncoder.encode(password));
            updated = true;
        }

        if (updated) {
            userService.saveUser(user);
        }

        return mapToProfileResponse(user);
    }

    private UserProfileResponse mapToProfileResponse(User user) {
        return UserProfileResponse.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .sentimentAnalysis(user.isSentimentAnalysis())
                .roles(user.getRoles())
                .totalJournalEntries(user.getJournalEntries() != null ? user.getJournalEntries().size() : 0)
                .build();
    }
}
