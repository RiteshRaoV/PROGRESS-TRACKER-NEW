package com.thbs.progress_tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatchWiseTopicProgressDTO {
    private long batchId;
    private long topicId;
    private double topicProgress;
}
