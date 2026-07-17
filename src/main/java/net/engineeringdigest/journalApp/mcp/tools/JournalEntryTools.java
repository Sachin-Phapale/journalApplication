package net.engineeringdigest.journalApp.mcp.tools;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.mcp.exception.McpToolException;
import net.engineeringdigest.journalApp.mcp.security.McpSecurityContext;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * MCP Tools for Journal Entry Operations.
 * Integrates with existing JournalEntryService and UserService.
 */
@Component
@Slf4j
public class JournalEntryTools {

    private final JournalEntryService journalEntryService;
    private final UserService userService;
    private final McpSecurityContext mcpSecurityContext;

    /**
     * Constructor injection for required services.
     */
    public JournalEntryTools(JournalEntryService journalEntryService,
                             UserService userService,
                             McpSecurityContext mcpSecurityContext) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
        this.mcpSecurityContext = mcpSecurityContext;
    }

    /**
     * Tool: Create a new journal entry.
     */
    public JournalEntry createJournalEntry(String title, String content) {
        if (title == null || title.trim().isEmpty()) {
            throw new McpToolException("Title cannot be empty");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new McpToolException("Content cannot be empty");
        }

        String username = mcpSecurityContext.getCurrentUsername();
        log.info("Creating journal entry for user: {}", username);

        JournalEntry entry = new JournalEntry();
        entry.setTitle(title);
        entry.setContent(content);

        journalEntryService.saveEntry(entry, username);
        return entry;
    }

    /**
     * Tool: Retrieve all journals of the authenticated user.
     */
    public List<JournalEntry> getMyJournalEntries() {
        String username = mcpSecurityContext.getCurrentUsername();
        log.info("Fetching journal entries for user: {}", username);

        User user = userService.findByUserName(username);
        if (user == null) {
            throw new McpToolException("User not found: " + username);
        }

        List<JournalEntry> entries = user.getJournalEntries();
        return entries != null ? entries : new ArrayList<>();
    }

    /**
     * Tool: Retrieve a specific journal entry by ID.
     */
    public JournalEntry getJournalById(String id) {
        ObjectId objectId = parseObjectId(id);
        String username = mcpSecurityContext.getCurrentUsername();
        User user = userService.findByUserName(username);

        if (user == null) {
            throw new McpToolException("User not found: " + username);
        }

        // Verify the journal entry belongs to the user
        boolean ownsEntry = user.getJournalEntries().stream()
                .anyMatch(entry -> entry.getId().equals(objectId));

        if (!ownsEntry) {
            throw new McpToolException("Journal entry not found or unauthorized");
        }

        Optional<JournalEntry> entryOpt = journalEntryService.findById(objectId);
        return entryOpt.orElseThrow(() -> new McpToolException("Journal entry not found in repository"));
    }

    /**
     * Tool: Update an existing journal entry.
     */
    public JournalEntry updateJournal(String id, String title, String content) {
        ObjectId objectId = parseObjectId(id);
        String username = mcpSecurityContext.getCurrentUsername();
        User user = userService.findByUserName(username);

        if (user == null) {
            throw new McpToolException("User not found: " + username);
        }

        // Verify ownership
        boolean ownsEntry = user.getJournalEntries().stream()
                .anyMatch(entry -> entry.getId().equals(objectId));

        if (!ownsEntry) {
            throw new McpToolException("Journal entry not found or unauthorized");
        }

        Optional<JournalEntry> entryOpt = journalEntryService.findById(objectId);
        if (!entryOpt.isPresent()) {
            throw new McpToolException("Journal entry not found in repository");
        }

        JournalEntry oldEntry = entryOpt.get();
        if (title != null && !title.trim().isEmpty()) {
            oldEntry.setTitle(title);
        }
        if (content != null && !content.trim().isEmpty()) {
            oldEntry.setContent(content);
        }

        journalEntryService.saveEntry(oldEntry);
        return oldEntry;
    }

    /**
     * Tool: Delete a specific journal entry.
     */
    public String deleteJournal(String id) {
        ObjectId objectId = parseObjectId(id);
        String username = mcpSecurityContext.getCurrentUsername();

        boolean removed = journalEntryService.deleteById(objectId, username);
        if (removed) {
            return "Journal Deleted successfully";
        } else {
            throw new McpToolException("Journal entry not found or delete operation failed");
        }
    }

    /**
     * Tool: Search journals by title, tag, or sentiment.
     */
    public List<JournalEntry> searchJournal(String title, String tag, String sentiment) {
        List<JournalEntry> myEntries = getMyJournalEntries();
        if (myEntries.isEmpty()) {
            return myEntries;
        }

        return myEntries.stream()
                .filter(entry -> {
                    boolean match = true;
                    if (title != null && !title.trim().isEmpty()) {
                        match = entry.getTitle() != null && entry.getTitle().toLowerCase().contains(title.toLowerCase());
                    }
                    if (match && tag != null && !tag.trim().isEmpty()) {
                        // Search for the tag in the content (e.g. matching "#tag" or word)
                        match = entry.getContent() != null && entry.getContent().toLowerCase().contains(tag.toLowerCase());
                    }
                    if (match && sentiment != null && !sentiment.trim().isEmpty()) {
                        try {
                            Sentiment searchSentiment = Sentiment.valueOf(sentiment.toUpperCase());
                            match = entry.getSentiment() == searchSentiment;
                        } catch (IllegalArgumentException e) {
                            // If invalid sentiment value, treat as unmatched
                            match = false;
                        }
                    }
                    return match;
                })
                .collect(Collectors.toList());
    }

    private ObjectId parseObjectId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new McpToolException("ID parameter is required");
        }
        try {
            return new ObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new McpToolException("Invalid ObjectId format: " + id);
        }
    }
}
