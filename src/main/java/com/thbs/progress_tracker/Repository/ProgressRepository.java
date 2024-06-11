package com.thbs.progress_tracker.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.thbs.progress_tracker.Entity.Progress;

import java.util.Optional;

public interface ProgressRepository extends MongoRepository<Progress, String> {
    Optional<Progress> findByUserId(Long userId);
}

