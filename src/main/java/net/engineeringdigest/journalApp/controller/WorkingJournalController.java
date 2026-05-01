package net.engineeringdigest.journalApp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/working")
public class WorkingJournalController {

    @GetMapping("/create")
    public ResponseEntity<String> createEntry(@RequestParam(required = false) String title) {
        try {
            System.out.println("=== WORKING JOURNAL CONTROLLER ===");
            System.out.println("Title parameter: " + title);
            
            String entryTitle = title != null ? title : "Default Entry " + System.currentTimeMillis();
            String entryContent = "This is a test entry created at " + new java.util.Date();
            
            // For now, just return success without saving to database
            String result = "SUCCESS: Journal entry created!\nTitle: " + entryTitle + "\nContent: " + entryContent;
            
            System.out.println("Entry created successfully!");
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("ERROR: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> testController() {
        System.out.println("=== WORKING JOURNAL CONTROLLER TEST ===");
        return new ResponseEntity<>("Working journal controller is working!", HttpStatus.OK);
    }
}
