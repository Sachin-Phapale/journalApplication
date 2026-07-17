package net.engineeringdigest.journalApp.mcp.tools;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.mcp.exception.McpToolException;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.stereotype.Component;

/**
 * MCP Tool for Weather Integration.
 * Reuses the application's existing WeatherService.
 */
@Component
@Slf4j
public class WeatherTools {

    private final WeatherService weatherService;

    /**
     * Constructor injection.
     */
    public WeatherTools(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Tool: Get today's weather.
     *
     * @param city the city name to retrieve weather for
     * @return the WeatherResponse containing temperature, conditions, etc.
     */
    public WeatherResponse todaysWeather(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new McpToolException("City parameter cannot be empty");
        }

        log.info("Retrieving today's weather for city: {}", city);
        try {
            WeatherResponse weather = weatherService.getWeather(city);
            if (weather == null) {
                throw new McpToolException("Weather data not available for: " + city);
            }
            return weather;
        } catch (Exception e) {
            log.error("Error retrieving weather for {}: {}", city, e.getMessage());
            throw new McpToolException("Failed to retrieve weather: " + e.getMessage(), e);
        }
    }
}
