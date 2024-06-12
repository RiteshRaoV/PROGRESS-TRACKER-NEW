package com.thbs.progress_tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserBatchProgressDTO {
    private Long employeeId;
    private double overallProgress;

}
