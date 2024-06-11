package com.thbs.progress_tracker.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thbs.progress_tracker.Entity.BatchProgress;
import com.thbs.progress_tracker.Entity.CourseProgress;
import com.thbs.progress_tracker.Entity.Progress;
import com.thbs.progress_tracker.Entity.ResourceProgress;
import com.thbs.progress_tracker.Entity.TopicProgress;
import com.thbs.progress_tracker.Repository.ProgressRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProgressService {

    @Autowired
    private ProgressRepository progressRepository;

    public void updateProgress(Long userId, Long batchId, Long courseId, Long topicId, Long resourceId, double completionPercentage) {
        Progress progress = progressRepository.findByUserId(userId)
                .orElseGet(() -> new Progress(userId, new ArrayList<>()));

        BatchProgress batchProgress = findOrCreateBatchProgress(progress, batchId);
        CourseProgress courseProgress = findOrCreateCourseProgress(batchProgress, courseId);
        TopicProgress topicProgress = findOrCreateTopicProgress(courseProgress, topicId);

        updateResourceProgress(topicProgress, resourceId, completionPercentage);
        updateTopicProgress(topicProgress);
        updateCourseProgress(courseProgress);
        updateOverallProgress(batchProgress);
        
        progressRepository.save(progress);
    }

    public Optional<Progress> getProgressByUserId(Long userId) {
        return progressRepository.findByUserId(userId);
    }

    private BatchProgress findOrCreateBatchProgress(Progress progress, Long batchId) {
        return progress.getBatches().stream()
                .filter(bp -> bp.getBatchId().equals(batchId))
                .findFirst()
                .orElseGet(() -> {
                    BatchProgress newBatchProgress = new BatchProgress(batchId,0.0, new ArrayList<>());
                    progress.getBatches().add(newBatchProgress);
                    return newBatchProgress;
                });
    }

    private CourseProgress findOrCreateCourseProgress(BatchProgress batchProgress, Long courseId) {
        return batchProgress.getCourses().stream()
                .filter(cp -> cp.getCourseId().equals(courseId))
                .findFirst()
                .orElseGet(() -> {
                    CourseProgress newCourseProgress = new CourseProgress(courseId,0.0, new ArrayList<>());
                    batchProgress.getCourses().add(newCourseProgress);
                    return newCourseProgress;
                });
    }

    private TopicProgress findOrCreateTopicProgress(CourseProgress courseProgress, Long topicId) {
        return courseProgress.getTopics().stream()
                .filter(tp -> tp.getTopicId().equals(topicId))
                .findFirst()
                .orElseGet(() -> {
                    TopicProgress newTopicProgress = new TopicProgress(topicId,0.0, new ArrayList<>());
                    courseProgress.getTopics().add(newTopicProgress);
                    return newTopicProgress;
                });
    }

    private void updateResourceProgress(TopicProgress topicProgress, Long resourceId, double completionPercentage) {
        ResourceProgress resourceProgress = topicProgress.getResources().stream()
                .filter(rp -> rp.getResourceId().equals(resourceId))
                .findFirst()
                .orElseGet(() -> {
                    ResourceProgress newResourceProgress = new ResourceProgress(resourceId, completionPercentage);
                    topicProgress.getResources().add(newResourceProgress);
                    return newResourceProgress;
                });

        resourceProgress.setCompletionPercentage(completionPercentage);
    }

    private void updateTopicProgress(TopicProgress topicProgress) {
        double averageCompletion = topicProgress.getResources().stream()
                .mapToDouble(ResourceProgress::getCompletionPercentage)
                .average()
                .orElse(0.0);

        topicProgress.setCompletionPercentage(averageCompletion);
    }

    private void updateCourseProgress(CourseProgress courseProgress) {
        double averageCompletion = courseProgress.getTopics().stream()
                .mapToDouble(TopicProgress::getCompletionPercentage)
                .average()
                .orElse(0.0);

        courseProgress.setCompletionPercentage(averageCompletion);
    }

    private void updateOverallProgress(BatchProgress batchProgress) {
        double averageCompletion = batchProgress.getCourses().stream()
                .mapToDouble(CourseProgress::getCompletionPercentage)
                .average()
                .orElse(0.0);

        batchProgress.setOverallCompletionPercentage(averageCompletion);
    }
}

