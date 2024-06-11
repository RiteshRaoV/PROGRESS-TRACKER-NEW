package com.thbs.progress_tracker.Entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TopicProgress {
    private Long topicId;
    private double completionPercentage;
    private List<ResourceProgress> resources;

}

