package com.thbs.progress_tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BatchWiseCourseProgressDTO {
    private long batchId;
    private long courseId;
    private double courseProgress;
}
