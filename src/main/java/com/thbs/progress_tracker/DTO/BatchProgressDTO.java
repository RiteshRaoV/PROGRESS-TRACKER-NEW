package com.thbs.progress_tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BatchProgressDTO {
    private long batchId;
    private double batchProgress;
}
