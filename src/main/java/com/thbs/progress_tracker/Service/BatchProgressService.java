package com.thbs.progress_tracker.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.thbs.progress_tracker.DTO.UserCourseProgressDTO;
import com.thbs.progress_tracker.DTO.BatchProgressDTO;
import com.thbs.progress_tracker.DTO.BatchWiseCourseProgressDTO;
import com.thbs.progress_tracker.DTO.BatchWiseResourceProgressDTO;
import com.thbs.progress_tracker.DTO.BatchWiseTopicProgressDTO;
import com.thbs.progress_tracker.DTO.BatchesDTO;
import com.thbs.progress_tracker.DTO.UserBatchProgressDTO;
import com.thbs.progress_tracker.Entity.BatchProgress;
import com.thbs.progress_tracker.Entity.CourseProgress;
import com.thbs.progress_tracker.Entity.Progress;
import com.thbs.progress_tracker.Entity.ResourceProgress;
import com.thbs.progress_tracker.Entity.TopicProgress;
import com.thbs.progress_tracker.Repository.ProgressRepository;

@Service
public class BatchProgressService {

    @Value("${batchModule.uri}")
    public String batchModuleUri;

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<BatchProgressDTO> findBatchwiseProgress() {
        String uri = batchModuleUri;
        ResponseEntity<List<BatchesDTO>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<BatchesDTO>>() {
                });
        List<Long> batchIds = response.getBody().stream()
                .map(BatchesDTO::getBatchId)
                .collect(Collectors.toList());

        return batchIds.stream()
                .map(this::calculateBatchProgress)
                .collect(Collectors.toList());
    }

    public BatchProgressDTO calculateBatchProgress(long batchId) {
        List<Progress> progresses = progressRepository.findByBatchesBatchId(batchId);

        double batchProgress = progresses.stream()
                .flatMap(progress -> progress.getBatches().stream())
                .filter(batchProgresss -> batchProgresss.getBatchId().equals(batchId))
                .mapToDouble(BatchProgress::getOverallCompletionPercentage)
                .average()
                .orElse(0.0);

        return new BatchProgressDTO(batchId, batchProgress);
    }

    public List<UserBatchProgressDTO> calculateOverallBatchProgressAllUsers(Long batchId) {
        List<Progress> progresses = progressRepository.findByBatchesBatchId(batchId);
        return progresses.stream()
                .map(progress -> {
                    double overallProgress = progress.getBatches().stream()
                            .filter(b -> b.getBatchId().equals(batchId))
                            .findFirst()
                            .map(BatchProgress::getOverallCompletionPercentage)
                            .orElse(0.0); // Default to 0 if not found
                    return new UserBatchProgressDTO(progress.getUserId(), overallProgress);
                })
                .collect(Collectors.toList());
    }

    public List<UserCourseProgressDTO> calculateCourseProgressOfUsersInBatch(long batchId, long courseId) {
        List<Progress> progresses = progressRepository.findByBatchesBatchId(batchId);
        return progresses.stream()
                .flatMap(progress -> progress.getBatches().stream()
                        .filter(batch -> batch.getBatchId().equals(batchId))
                        .flatMap(batch -> batch.getCourses().stream().filter(c -> c.getCourseId().equals(courseId))
                                .map(course -> new UserCourseProgressDTO(
                                        progress.getUserId(),
                                        course.getCompletionPercentage()))))
                .collect(Collectors.toList());
    }

    public BatchWiseCourseProgressDTO getBatchWiseCourseProgress(long batchId, long courseId) {
        List<Progress> progresses = progressRepository.findByBatchesBatchId(batchId);
        double averageCourseProgress = progresses.stream()
                .flatMap(progress -> progress.getBatches().stream()
                        .filter(batch -> batch.getBatchId().equals(batchId))
                        .flatMap(batch -> batch.getCourses().stream()
                                .filter(course -> course.getCourseId().equals(courseId))
                                .map(CourseProgress::getCompletionPercentage)))
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return new BatchWiseCourseProgressDTO(batchId, courseId, averageCourseProgress);
    }

    public BatchWiseTopicProgressDTO getBatchWiseTopicProgress(long batchId, long courseId, long topicId) {
        List<Progress> progresses = progressRepository.findByBatchesBatchId(batchId);
        double averageTopicProgress = progresses.stream()
                .flatMap(progress -> progress.getBatches().stream()
                        .filter(batch -> batch.getBatchId().equals(batchId))
                        .flatMap(batch -> batch.getCourses().stream()
                                .filter(course -> course.getCourseId().equals(courseId))
                                .flatMap(course -> course.getTopics().stream()
                                        .filter(topic -> topic.getTopicId().equals(topicId))
                                        .map(TopicProgress::getCompletionPercentage))))
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return new BatchWiseTopicProgressDTO(batchId, topicId, averageTopicProgress);
    }

    public BatchWiseResourceProgressDTO getBatchWiseResourceProgress(long batchId, long courseId, long topicId,
            long resourceId) {
        List<Progress> progresses = progressRepository.findByBatchesBatchId(batchId);
        double averageTopicProgress = progresses.stream()
                .flatMap(progress -> progress.getBatches().stream())
                .filter(batch -> batch.getBatchId().equals(batchId))
                .flatMap(batch -> batch.getCourses().stream())
                .filter(course -> course.getCourseId().equals(courseId))
                .flatMap(course -> course.getTopics().stream())
                .filter(topic -> topic.getTopicId().equals(topicId))
                .flatMap(topic -> topic.getResources().stream())
                .filter(resource -> resource.getResourceId().equals(resourceId))
                .map(ResourceProgress::getCompletionPercentage)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return new BatchWiseResourceProgressDTO(batchId, resourceId, averageTopicProgress);
    }

}
