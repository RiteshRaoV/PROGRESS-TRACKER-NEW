package com.thbs.progress_tracker.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LearningPlanDTO {
    private long batchId;
    private List<BatchCoursesDTO> batchCourses;
}
