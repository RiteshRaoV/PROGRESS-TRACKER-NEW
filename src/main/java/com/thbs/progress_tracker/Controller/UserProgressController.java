package com.thbs.progress_tracker.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.thbs.progress_tracker.DTO.CourseOverallProgressDTO;
import com.thbs.progress_tracker.DTO.DeleteProgressOfUsersDTO;
import com.thbs.progress_tracker.DTO.UpdateNewUsersFromBatchDTO;
import com.thbs.progress_tracker.DTO.UserCourseTopicOverallProgressDTO;
import com.thbs.progress_tracker.DTO.UserCourseTopicResourceOverallProgressDTO;
import com.thbs.progress_tracker.DTO.UserOverallProgressDTO;
import com.thbs.progress_tracker.Entity.Progress;
import com.thbs.progress_tracker.Service.UserProgressService;

import io.swagger.v3.oas.annotations.Operation;

import java.util.Optional;

@RestController
@RequestMapping("/user-progress")
public class UserProgressController {

    @Autowired
    private UserProgressService progressService;

    @PostMapping("/update")
    @Operation(summary = "handles the updation and creation of the progress records")
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

    @GetMapping("/user/{userId}/batch/{batchId}")
    @Operation(summary = "gives the overall progress of the user")
    public ResponseEntity<UserOverallProgressDTO> getUserOverallProgress(@PathVariable Long userId,
            @PathVariable Long batchId) {
        double batchOverallProgress = progressService.calculateUserOverallProgress(userId, batchId);
        UserOverallProgressDTO response = new UserOverallProgressDTO(userId, batchId, batchOverallProgress);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/batch/{batchId}/course/{courseId}")
    @Operation(summary = "gives the overall progress of the user in a particular course")
    public ResponseEntity<CourseOverallProgressDTO> getUserCourseOverallProgress(@PathVariable Long userId,
            @PathVariable Long batchId, @PathVariable Long courseId) {
        double courseOverallProgress = progressService.calculateUserCourseOverallProgress(userId, batchId, courseId);
        CourseOverallProgressDTO response = new CourseOverallProgressDTO(userId, batchId, courseId,
                courseOverallProgress);

        return ResponseEntity.ok(response);
    }

    // endpoint is changed new pathVariable add courseId
    @GetMapping("/user/{userId}/batch/{batchId}/course/{courseId}/topic/{topicId}")
    @Operation(summary = "gives the overall progress of the user in a particular topic")
    public ResponseEntity<UserCourseTopicOverallProgressDTO> getUserCourseTopicOverallProgress(
            @PathVariable Long userId,
            @PathVariable Long batchId, @PathVariable Long courseId, @PathVariable Long topicId) {
        double topicOverallProgress = progressService.calculateUserCourseTopicOverallProgress(userId, batchId, courseId,
                topicId);
        UserCourseTopicOverallProgressDTO response = new UserCourseTopicOverallProgressDTO(userId, batchId, courseId,
                topicId,
                topicOverallProgress);

        return ResponseEntity.ok(response);
    }

    // endpoint is changed new pathVariable add courseId and topicId required
    @GetMapping("/user/{userId}/batch/{batchId}/course/{courseId}/topic/{topicId}/resource/{resourceId}")
    @Operation(summary = "gives the overall progress of the user in a particular resource")
    public ResponseEntity<UserCourseTopicResourceOverallProgressDTO> getUserCourseTopicResourceOverallProgress(
            @PathVariable Long userId,
            @PathVariable Long batchId, @PathVariable Long courseId, @PathVariable Long topicId,
            @PathVariable Long resourceId) {
        double resourceOverallProgress = progressService.calculateUserCourseTopicResourceOverallProgress(userId,
                batchId, courseId,
                topicId, resourceId);
        UserCourseTopicResourceOverallProgressDTO response = new UserCourseTopicResourceOverallProgressDTO(userId,
                batchId, courseId,
                topicId, resourceId, resourceOverallProgress);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-progress")
    @Operation(summary = "handles the deletion of progress records if the user is removed from the batch")
    public ResponseEntity<?> deleteProgressForUsers(@RequestBody DeleteProgressOfUsersDTO deleteUsers) {
        progressService.deleteProgressOfUsers(deleteUsers.getUserIds(), deleteUsers.getBatchId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-progress")
    public ResponseEntity<?> createProgressForNewUsers(@RequestBody UpdateNewUsersFromBatchDTO newUsers) {
        progressService.setProgressForNewUsers(newUsers.getUserIds(), newUsers.getBatchId());
        return ResponseEntity.ok().build();
    }
}
