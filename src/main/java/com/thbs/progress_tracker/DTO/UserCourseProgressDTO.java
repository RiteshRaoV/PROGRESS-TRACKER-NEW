package com.thbs.progress_tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseProgressDTO {
    private long userId;
    private double courseProgress;
    
}