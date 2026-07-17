package net.engineeringdigest.journalApp.mcp.tools;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.mcp.exception.McpToolException;
import net.engineeringdigest.journalApp.service.EmailService;
import org.springframework.stereotype.Component;

/**
 * MCP Tool for Email dispatch.
 * Reuses the existing EmailService.
 */
@Component
@Slf4j
public class EmailTools {

    private final EmailService emailService;

    /**
     * Constructor injection.
     */
    public EmailTools(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Tool: Send email to a specified recipient.
     *
     * @param to      the recipient's email address
     * @param subject the email subject
     * @param body    the email body content
     * @return success statement
     */
    public String sendEmail(String to, String subject, String body) {
        if (to == null || to.trim().isEmpty()) {
            throw new McpToolException("Recipient email ('to') cannot be empty");
        }
        if (subject == null || subject.trim().isEmpty()) {
            throw new McpToolException("Email subject cannot be empty");
        }
        if (body == null || body.trim().isEmpty()) {
            throw new McpToolException("Email body cannot be empty");
        }

        log.info("Sending email to: {} with subject: {}", to, subject);
        try {
            emailService.sendEmail(to, subject, body);
            return "Email sent successfully to " + to;
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new McpToolException("Failed to dispatch email: " + e.getMessage(), e);
        }
    }
}
