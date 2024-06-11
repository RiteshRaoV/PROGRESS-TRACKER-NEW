package com.thbs.progress_tracker.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Document(collection = "progress")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Progress {

    @Id
    private String id;
    private Long userId;
    private List<BatchProgress> batches;

    public Progress(Long userId, List<BatchProgress> batches) {
        this.userId=userId;
        this.batches=batches;
    }
}

