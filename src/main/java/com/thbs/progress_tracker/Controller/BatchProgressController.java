package com.thbs.progress_tracker.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thbs.progress_tracker.DTO.BatchProgressDTO;
import com.thbs.progress_tracker.Service.BatchProgressService;
import com.thbs.progress_tracker.Service.UserProgressService;

@RestController
@RequestMapping("/batch-progress")
public class BatchProgressController {

    @Autowired
    private BatchProgressService batchProgressService;

    // @GetMapping
    // public ResponseEntity<?> calculateBatchwiseProgress() {
    //     // List<BatchProgressDTO> batchProgressList = batchProgressService.findBatchesProgress();
    //     if (!batchProgressList.isEmpty()) {
    //         return ResponseEntity.ok(batchProgressList);
    //     } else {
    //         String errorMessage = "No batches found.";
    //         return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorMessage);
    //     }
    // }
}
