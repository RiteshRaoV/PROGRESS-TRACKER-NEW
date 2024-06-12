package com.thbs.progress_tracker.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UpdateNewUsersFromBatchDTO
 */
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class UpdateNewUsersFromBatchDTO {
    private long batchId;
    private List<Long> userIds;
    
}
