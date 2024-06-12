package com.thbs.progress_tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CourseOverallProgressDTO {
    private Long userId;
    private Long batchId;
    private Long courseId;
    private double courseProgress;

}
