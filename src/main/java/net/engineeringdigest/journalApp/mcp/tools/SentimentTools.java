package net.engineeringdigest.journalApp.mcp.tools;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.mcp.dto.SentimentResponse;
import net.engineeringdigest.journalApp.mcp.exception.McpToolException;
import org.springframework.stereotype.Component;

/**
 * MCP Tool for Sentiment Analysis.
 * Implements a local semantic parser evaluating positive and negative word occurrences
 * to return classifications (Positive, Negative, Neutral) and a confidence score.
 */
@Component
@Slf4j
public class SentimentTools {

    /**
     * Tool: Analyze the sentiment of journal content.
     *
     * @param content the journal text content to analyze
     * @return the sentiment response containing sentiment type and score
     */
    public SentimentResponse analyzeSentiment(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new McpToolException("Content cannot be empty");
        }

        log.info("Analyzing sentiment of content length: {}", content.length());

        String lower = content.toLowerCase();
        int positiveCount = 0;
        int negativeCount = 0;

        // Semantic word sets
        String[] positiveWords = {"happy", "great", "good", "love", "excited", "wonderful", "joy", "glad", "awesome", "perfect", "blessed", "pleasant", "nice"};
        String[] negativeWords = {"sad", "bad", "hate", "angry", "anxious", "depressed", "terrible", "worst", "sorry", "fear", "hurt", "pain", "unhappy"};

        for (String word : positiveWords) {
            if (lower.contains(word)) {
                positiveCount++;
            }
        }

        for (String word : negativeWords) {
            if (lower.contains(word)) {
                negativeCount++;
            }
        }

        String sentiment;
        double score;

        int totalCount = positiveCount + negativeCount;
        if (totalCount == 0) {
            sentiment = "Neutral";
            score = 0.5;
        } else if (positiveCount > negativeCount) {
            sentiment = "Positive";
            score = 0.5 + (0.5 * ((double) positiveCount / totalCount));
        } else if (negativeCount > positiveCount) {
            sentiment = "Negative";
            score = 0.5 - (0.5 * ((double) negativeCount / totalCount));
            if (score < 0.0) {
                score = 0.0;
            }
        } else {
            sentiment = "Neutral";
            score = 0.5;
        }

        // Ensure rounding to 2 decimal places
        score = Math.round(score * 100.0) / 100.0;

        return new SentimentResponse(sentiment, score);
    }
}
