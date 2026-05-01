package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/journal")
public class SimpleJournalController {

    @Autowired
    private JournalEntryService journalEntryService;

    @PostMapping("/create")
    public ResponseEntity<String> createJournalEntry(@RequestParam String title, @RequestParam(required = false) String content) {
        try {
            System.out.println("=== SIMPLE JOURNAL CONTROLLER ===");
            System.out.println("Title: " + title);
            System.out.println("Content: " + content);
            
            JournalEntry entry = new JournalEntry();
            entry.setTitle(title != null ? title : "Untitled Entry");
            entry.setContent(content != null ? content : "");
            
            journalEntryService.saveEntry(entry, "testuser");
            System.out.println("Entry saved successfully!");
            
            return new ResponseEntity<>("Journal entry created successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testController() {
        System.out.println("=== SIMPLE JOURNAL CONTROLLER TEST ===");
        return new ResponseEntity<>("Simple journal controller is working!", HttpStatus.OK);
    }
    
    @GetMapping("/create")
    public ResponseEntity<String> createJournalEntryGet(@RequestParam(required = false) String title, @RequestParam(required = false) String content) {
        try {
            System.out.println("=== CREATE JOURNAL ENTRY GET ===");
            System.out.println("Title: " + title);
            System.out.println("Content: " + content);
            
            // Create a simple entry without any complex operations
            JournalEntry entry = new JournalEntry();
            entry.setTitle(title != null ? title : "Test Entry " + System.currentTimeMillis());
            entry.setContent(content != null ? content : "Test content created at " + new java.util.Date());
            
            // Save with hardcoded user to avoid authentication issues
            journalEntryService.saveEntry(entry, "testuser");
            System.out.println("Entry saved successfully with ID: " + entry.getId());
            
            return new ResponseEntity<>("SUCCESS: Journal entry created! ID: " + entry.getId(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("ERROR: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
