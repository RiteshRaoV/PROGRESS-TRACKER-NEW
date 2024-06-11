package com.thbs.progress_tracker.Entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResourceProgress {
    private Long resourceId;
    private double completionPercentage;
}
