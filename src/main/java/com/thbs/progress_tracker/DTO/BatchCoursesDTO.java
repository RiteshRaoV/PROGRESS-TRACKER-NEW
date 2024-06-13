package com.thbs.progress_tracker.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BatchCoursesDTO {
    private long courseId;
    private List<BatchTopicDTO> topic;
}
