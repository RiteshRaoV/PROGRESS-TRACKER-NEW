package com.thbs.progress_tracker.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.thbs.progress_tracker.Entity.Progress;
import com.thbs.progress_tracker.Service.UserProgressService;

import java.util.Optional;

@RestController
@RequestMapping("/user-progress")
public class UserProgressController {

    @Autowired
    private UserProgressService progressService;

    @PostMapping("/update")
    public void updateProgress(@RequestParam Long userId, 
                               @RequestParam Long batchId, 
                               @RequestParam Long courseId, 
                               @RequestParam Long topicId, 
                               @RequestParam Long resourceId, 
                               @RequestParam double completionPercentage) {
        progressService.updateProgress(userId, batchId, courseId, topicId, resourceId, completionPercentage);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getProgress(@PathVariable Long userId) {
        Optional<Progress> progress = progressService.getProgressByUserId(userId);
        
        if (progress.isPresent()) {
            return ResponseEntity.ok(progress.get());
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data found");
        }
    }
    
}

