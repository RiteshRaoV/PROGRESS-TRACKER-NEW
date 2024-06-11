package com.thbs.progress_tracker.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thbs.progress_tracker.DTO.BatchProgressDTO;
import com.thbs.progress_tracker.DTO.BatchWiseCourseProgressDTO;
import com.thbs.progress_tracker.DTO.BatchWiseResourceProgressDTO;
import com.thbs.progress_tracker.DTO.BatchWiseTopicProgressDTO;
import com.thbs.progress_tracker.DTO.UserBatchProgressDTO;
import com.thbs.progress_tracker.DTO.UserCourseProgressDTO;
import com.thbs.progress_tracker.Service.BatchProgressService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/batch-progress")
public class BatchProgressController {

    @Autowired
    private BatchProgressService batchProgressService;

    // gives the overall progress of all the batches
    @Operation(summary = "gives the overall progress of all the batches")
    @GetMapping
    public ResponseEntity<?> calculateBatchwiseProgress() {
        List<BatchProgressDTO> batchProgressList = batchProgressService.findBatchwiseProgress();
        if (!batchProgressList.isEmpty()) {
            return ResponseEntity.ok(batchProgressList);
        } else {
            String errorMessage = "No batches found.";
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorMessage);
        }
    }

    @GetMapping("/{batchId}")
    public ResponseEntity<BatchProgressDTO> calculateBatchProgress(@PathVariable int batchId) {
        BatchProgressDTO progress = batchProgressService.calculateBatchProgress(batchId);
        if (progress != null) {
            return ResponseEntity.ok(progress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

        // gives the overall progress of all the users in the batch
    @Operation(summary  = "gives the overall progress of all the users in the batch")
    @GetMapping("/allusers/{batchId}")
    public ResponseEntity<List<UserBatchProgressDTO>> getOverallBatchProgress(@PathVariable Long batchId) {
        List<UserBatchProgressDTO> progressList = batchProgressService.calculateOverallBatchProgressAllUsers(batchId);
        if (!progressList.isEmpty()) {
            return ResponseEntity.ok(progressList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all-users/{batchId}/course/{courseId}")
    @Operation(summary  = "gives the overall course progress of all the users in the batch")
    public ResponseEntity<List<UserCourseProgressDTO>> getCourseProgressOfUsersInBatch(@PathVariable long batchId ,@PathVariable long courseId){
        List<UserCourseProgressDTO> progress=batchProgressService.calculateCourseProgressOfUsersInBatch(batchId,courseId);
        if (!progress.isEmpty()) {
            return ResponseEntity.ok(progress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/batch-course-progress/batch/{batchId}/course/{courseId}")
    @Operation(summary  = "gives the overall course progress of a course in a batch")
    public ResponseEntity<BatchWiseCourseProgressDTO> getOverallBatchWiseCourseProgress(@PathVariable long batchId,@PathVariable long courseId){
        BatchWiseCourseProgressDTO progress=batchProgressService.getBatchWiseCourseProgress(batchId, courseId);
        if(progress!=null){
            return ResponseEntity.ok(progress);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/batch-topic-progress/batch/{batchId}/course/{courseId}/topic/{topicId}")
    @Operation(summary  = "gives the overall topic progress of a topic in a batch")
    public ResponseEntity<BatchWiseTopicProgressDTO> getOverallbatchWiseTopicProgress(@PathVariable long batchId,@PathVariable long courseId, @PathVariable long topicId){
        BatchWiseTopicProgressDTO progress=batchProgressService.getBatchWiseTopicProgress(batchId,courseId,topicId);
        if(progress!=null){
            return ResponseEntity.ok(progress);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/batch-resource-progress/batch/{batchId}/course/{courseId}/topic/{topicId}resource/{resourceId}")
    @Operation(summary  = "gives the overall resource progress of a resource in a batch")
    public ResponseEntity<BatchWiseResourceProgressDTO> getOverallBatchWiseResourceProgress(@PathVariable long batchId,@PathVariable long courseId,@PathVariable long topicId,@PathVariable long resourceId){
        BatchWiseResourceProgressDTO progress = batchProgressService.getBatchWiseResourceProgress(batchId,courseId,topicId, resourceId);
        if(progress!=null){
            return ResponseEntity.ok(progress);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
