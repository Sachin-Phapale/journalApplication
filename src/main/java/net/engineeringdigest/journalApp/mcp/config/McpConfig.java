package net.engineeringdigest.journalApp.mcp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.json.jackson2.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import net.engineeringdigest.journalApp.mcp.response.McpResponse;
import net.engineeringdigest.journalApp.mcp.tools.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Spring Configuration for the MCP (Model Context Protocol) Server.
 * Exposes the synchronous McpSyncServer bean, configures the STDIO transport layer,
 * and handles registration of all 12 business logic tools.
 */
@Configuration
public class McpConfig {

    @Value("${mcp.server.name:Journal App MCP Server}")
    private String serverName;

    @Value("${mcp.server.version:1.0.0}")
    private String serverVersion;

    @Value("${mcp.server.transport:stdio}")
    private String transportType;

    private final JournalEntryTools journalEntryTools;
    private final SentimentTools sentimentTools;
    private final WeatherTools weatherTools;
    private final EmailTools emailTools;
    private final UserProfileTools userProfileTools;
    private final AdminTools adminTools;
    private final ObjectMapper objectMapper;

    /**
     * Constructor injection.
     */
    public McpConfig(JournalEntryTools journalEntryTools,
                     SentimentTools sentimentTools,
                     WeatherTools weatherTools,
                     EmailTools emailTools,
                     UserProfileTools userProfileTools,
                     AdminTools adminTools,
                     ObjectMapper objectMapper) {
        this.journalEntryTools = journalEntryTools;
        this.sentimentTools = sentimentTools;
        this.weatherTools = weatherTools;
        this.emailTools = emailTools;
        this.userProfileTools = userProfileTools;
        this.adminTools = adminTools;
        this.objectMapper = objectMapper;
    }

    /**
     * Define the McpSyncServer Bean.
     * Hooks up the standard input/output transport, configures capabilities,
     * registers tools, and starts the IO listener loop.
     *
     * @return initialized McpSyncServer
     */
    @Bean
    public McpSyncServer mcpSyncServer() {
        java.io.PrintStream originalOut = net.engineeringdigest.journalApp.JournalApplication.originalOut;
        if (originalOut == null) {
            originalOut = System.out;
        }

        JacksonMcpJsonMapper mapper = new JacksonMcpJsonMapper(objectMapper);
        
        // Use custom streams constructor to isolate MCP communication to original standard out
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider(
                mapper,
                System.in,
                originalOut
        );

        ToolAccumulator accumulator = new ToolAccumulator();
        registerTools(accumulator);

        McpSyncServer server = McpServer.sync(transportProvider)
                .serverInfo(serverName, serverVersion)
                .capabilities(McpSchema.ServerCapabilities.builder().tools(true).build())
                .tools(accumulator.tools)
                .build();

        return server;
    }

    private static class ToolAccumulator {
        final java.util.List<McpServerFeatures.SyncToolSpecification> tools = new java.util.ArrayList<>();
        public void addTool(McpServerFeatures.SyncToolSpecification spec) {
            tools.add(spec);
        }
    }

    /**
     * Define and register the 12 required MCP Tools.
     */
    private void registerTools(ToolAccumulator server) {
        // 1. Create Journal Entry
        server.addTool(new McpServerFeatures.SyncToolSpecification(
            createTool(
                "create_journal_entry",
                "Create a new journal entry for the authenticated user",
                "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"title\":{\"type\":\"string\",\"description\":\"Title of the journal entry\"},\"content\":{\"type\":\"string\",\"description\":\"Body content of the journal entry\"}},\"required\":[\"title\",\"content\"]}"
            ),
            (exchange, request) -> {
                try {
                    Map<String, Object> args = request.arguments();
                    String title = (String) args.get("title");
                    String content = (String) args.get("content");
                    var entry = journalEntryTools.createJournalEntry(title, content);
                    return McpResponse.successJson(entry);
                } catch (Exception e) {
                    return McpResponse.error(e.getMessage());
                }
            }
        ));

        // 2. Get My Journal Entries
        server.addTool(new McpServerFeatures.SyncToolSpecification(
            createTool(
                "get_my_journal_entries",
                "Retrieve all journal entries of the authenticated user",
                "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{}}"
            ),
            (exchange, request) -> {
                try {
                    var entries = journalEntryTools.getMyJournalEntries();
                    return McpResponse.successJson(entries);
                } catch (Exception e) {
                    return McpResponse.error(e.getMessage());
                }
            }
        ));

        // 3. Get Journal By ID
        server.addTool(new McpServerFeatures.SyncToolSpecification(
            createTool(
                "get_journal_by_id",
                "Retrieve a specific journal entry by its ID for the authenticated user",
                "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"id\":{\"type\":\"string\",\"description\":\"Hex string representation of the journal ID\"}},\"required\":[\"id\"]}"
            ),
            (exchange, request) -> {
                try {
                    Map<String, Object> args = request.arguments();
                    String id = (String) args.get("id");
                    var entry = journalEntryTools.getJournalById(id);
                    return McpResponse.successJson(entry);
                } catch (Exception e) {
                    return McpResponse.error(e.getMessage());
                }
            }
        ));

        // 4. Update Journal
        server.addTool(new McpServerFeatures.SyncToolSpecification(
            createTool(
                "update_journal",
                "Update an existing journal entry's title and/or content",
                "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"id\":{\"type\":\"string\",\"description\":\"Journal ID\"},\"title\":{\"type\":\"string\",\"description\":\"New title\"},\"content\":{\"type\":\"string\",\"description\":\"New content\"}},\"required\":[\"id\"]}"
            ),
            (exchange, request) -> {
                try {
                    Map<String, Object> args = request.arguments();
                    String id = (String) args.get("id");
                    String title = (String) args.get("title");
                    String content = (String) args.get("content");
                    var entry = journalEntryTools.updateJournal(id, title, content);
                    return McpResponse.successJson(entry);
                } catch (Exception e) {
                    return McpResponse.error(e.getMessage());
                }
            }
        ));

        // 5. Delete Journal
        server.addTool(new McpServerFeatures.SyncToolSpecification(
            createTool(
                "delete_journal",
                "Delete a specific journal entry by its ID",
                "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"id\":{\"type\":\"string\",\"description\":\"Journal ID\"}},\"required\":[\"id\"]}"
            ),
            (exchange, request) -> {
                try {
                    Map<String, Object> args = request.arguments();
                    String id = (String) args.get("id");
                    String result = journalEntryTools.deleteJournal(id);
                    return McpResponse.success(result);
                } catch (Exception e) {
                    return McpResponse.error(e.getMessage());
                }
            }
        ));

        // 6. Search Journal
        server.addTool(new McpServerFeatures.SyncToolSpecification(
            createTool(
                "search_journal",
                "Search journal entries by title, tag, or sentiment filter keywords",
                "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"title\":{\"type\":\"string\",\"description\":\"Title keyword\"},\"tag\":{\"type\":\"string\",\"description\":\"Content/tag keyword\"},\"sentiment\":{\"type\":\"string\",\"description\":\"Sentiment value (HAPPY, SAD, ANGRY, ANXIOUS)\"}}}"
            ),
            (exchange, request) -> {
                try {
                    Map<String, Object> args = request.arguments();
                    String title = (String) args.get("title");
                    String tag = (String) args.get("tag");
                    String sentiment = (String) args.get("sentiment");
                    var entries = journalEntryTools.searchJournal(title, tag, sentiment);
                    return McpResponse.successJson(entries);
                } catch (Exception e) {
                    return McpResponse.error(e.getMessage());
                }
            }
        ));

        // 7. Analyze Sentiment
        server.addTool(new McpServerFeatures.SyncToolSpecification(
            createTool(
                "analyze_sentiment",
                "Perform sentiment analysis on the provided journal content text",
                "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"content\":{\"type\":\"string\",\"description\":\"Journal content to analyze\"}},\"required\":[\"content\"]}"
            ),
            (exchange, request) -> {
                try {
                    Map<String, Object> args = request.arguments();
                    String content = (String) args.get("content");
                    var response = sentimentTools.analyzeSentiment(content);
                    return McpResponse.successJson(response);
                } catch (Exception e) {
                    return McpResponse.error(e.getMessage());
                }
            }
        ));

        // 8. Today's Weather
        server.addTool(new McpServerFeatures.SyncToolSpecification(
            createTool(
                "todays_weather",
                "Retrieve the weather forecast for a given city",
                "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"city\":{\"type\":\"string\",\"description\":\"City name\"}},\"required\":[\"city\"]}"
            ),
            (exchange, request) -> {
                try {
                    Map<String, Object> args = request.arguments();
                    String city = (String) args.get("city");
                    var response = weatherTools.todaysWeather(city);
                    return McpResponse.successJson(response);
                } catch (Exception e) {
                    return McpResponse.error(e.getMessage());
                }
            }
        ));

        // 9. Send Email
        server.addTool(new McpServerFeatures.SyncToolSpecification(
            createTool(
                "send_email",
                "Send an email with subject and body to the recipient address",
                "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"to\":{\"type\":\"string\",\"description\":\"Recipient email address\"},\"subject\":{\"type\":\"string\",\"description\":\"Email subject line\"},\"body\":{\"type\":\"string\",\"description\":\"Body content of the email\"}},\"required\":[\"to\",\"subject\",\"body\"]}"
            ),
            (exchange, request) -> {
                try {
                    Map<String, Object> args = request.arguments();
                    String to = (String) args.get("to");
                    String subject = (String) args.get("subject");
                    String body = (String) args.get("body");
                    String result = emailTools.sendEmail(to, subject, body);
                    return McpResponse.success(result);
                } catch (Exception e) {
                    return McpResponse.error(e.getMessage());
                }
            }
        ));

        // 10. Get User Profile
        server.addTool(new McpServerFeatures.SyncToolSpecification(
            createTool(
                "get_user_profile",
                "Retrieve profile settings of the currently authenticated user",
                "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{}}"
            ),
            (exchange, request) -> {
                try {
                    var profile = userProfileTools.getUserProfile();
                    return McpResponse.successJson(profile);
                } catch (Exception e) {
                    return McpResponse.error(e.getMessage());
                }
            }
        ));

        // 11. Update User Profile
        server.addTool(new McpServerFeatures.SyncToolSpecification(
            createTool(
                "update_user_profile",
                "Update the authenticated user's email, password, or sentiment analysis options",
                "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"email\":{\"type\":\"string\",\"description\":\"New email address\"},\"password\":{\"type\":\"string\",\"description\":\"New password\"},\"sentimentAnalysis\":{\"type\":\"boolean\",\"description\":\"Enable or disable sentiment analysis\"}}}"
            ),
            (exchange, request) -> {
                try {
                    Map<String, Object> args = request.arguments();
                    String email = (String) args.get("email");
                    String password = (String) args.get("password");
                    Boolean sentimentAnalysis = (Boolean) args.get("sentimentAnalysis");
                    var profile = userProfileTools.updateUserProfile(email, password, sentimentAnalysis);
                    return McpResponse.successJson(profile);
                } catch (Exception e) {
                    return McpResponse.error(e.getMessage());
                }
            }
        ));

        // 12. Admin Actions: get_all_users, delete_user, statistics
        server.addTool(new McpServerFeatures.SyncToolSpecification(
            createTool(
                "admin_get_all_users",
                "Retrieve a list of all users in the system (requires ADMIN role)",
                "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{}}"
            ),
            (exchange, request) -> {
                try {
                    var users = adminTools.getAllUsers();
                    return McpResponse.successJson(users);
                } catch (Exception e) {
                    return McpResponse.error(e.getMessage());
                }
            }
        ));

        server.addTool(new McpServerFeatures.SyncToolSpecification(
            createTool(
                "admin_delete_user",
                "Delete a specific user by username (requires ADMIN role)",
                "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"userName\":{\"type\":\"string\",\"description\":\"Username of the user to delete\"}},\"required\":[\"userName\"]}"
            ),
            (exchange, request) -> {
                try {
                    Map<String, Object> args = request.arguments();
                    String userName = (String) args.get("userName");
                    String result = adminTools.deleteUser(userName);
                    return McpResponse.success(result);
                } catch (Exception e) {
                    return McpResponse.error(e.getMessage());
                }
            }
        ));

        server.addTool(new McpServerFeatures.SyncToolSpecification(
            createTool(
                "admin_get_statistics",
                "Retrieve dashboard statistics counts of users, journals, averages, and active users (requires ADMIN role)",
                "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{}}"
            ),
            (exchange, request) -> {
                try {
                    var stats = adminTools.getStatistics();
                    return McpResponse.successJson(stats);
                } catch (Exception e) {
                    return McpResponse.error(e.getMessage());
                }
            }
        ));
    }

    private McpSchema.Tool createTool(String name, String description, String schemaJson) {
        try {
            McpSchema.JsonSchema inputSchema = objectMapper.readValue(schemaJson, McpSchema.JsonSchema.class);
            return new McpSchema.Tool(
                name,
                null,
                description,
                inputSchema,
                null,
                null,
                null
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse schema for tool: " + name, e);
        }
    }
}
