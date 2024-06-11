package com.thbs.progress_tracker.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.thbs.progress_tracker.DTO.BatchProgressDTO;
import com.thbs.progress_tracker.Entity.Progress;
import com.thbs.progress_tracker.Repository.ProgressRepository;

@Service
public class BatchProgressService {

    @Value("${batches.uri}")
    String batchModuleUri;

    @Autowired
    private ProgressRepository progressRepository;

    // public List<BatchProgressDTO> findBatchwiseProgress() {
    //     String uri = batchModuleUri;
    //     ResponseEntity<List<Long>> response = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Long>>() {}, buName);

        
    //     List<Progress> progresses = progressRepository.findByUserIdIn();
    // }


}
