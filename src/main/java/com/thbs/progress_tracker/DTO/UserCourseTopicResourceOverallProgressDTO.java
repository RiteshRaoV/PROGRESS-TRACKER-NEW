package com.thbs.progress_tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseTopicResourceOverallProgressDTO {
    private Long userId;
    private Long batchId;
    private Long courseId;
    private Long topicId;
    private Long resourceId;
    private double resourceProgress;
}
