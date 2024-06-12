package com.thbs.progress_tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatchWiseResourceProgressDTO {
    private long batchId;
    private long resourceId;
    private double resourceProgress;
}
