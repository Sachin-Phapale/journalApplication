package net.engineeringdigest.journalApp.mcp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Output DTO for sentiment analysis.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentResponse {
    private String sentiment; // Positive, Negative, Neutral
    private double score;     // Sentiment score from 0.0 to 1.0
}
