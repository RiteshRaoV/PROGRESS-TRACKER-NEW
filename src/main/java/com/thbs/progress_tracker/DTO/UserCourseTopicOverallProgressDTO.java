package com.thbs.progress_tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseTopicOverallProgressDTO {
    private Long userId;
    private Long batchId;
    private Long courseId;
    private Long topicId;
    private double completionPercentage;
}
