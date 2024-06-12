package com.thbs.progress_tracker.ServiceTest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.thbs.progress_tracker.DTO.BatchProgressDTO;
import com.thbs.progress_tracker.DTO.BatchWiseCourseProgressDTO;
import com.thbs.progress_tracker.DTO.BatchWiseResourceProgressDTO;
import com.thbs.progress_tracker.DTO.BatchWiseTopicProgressDTO;
import com.thbs.progress_tracker.DTO.BatchesDTO;
import com.thbs.progress_tracker.DTO.UserBatchProgressDTO;
import com.thbs.progress_tracker.DTO.UserCourseProgressDTO;
import com.thbs.progress_tracker.Entity.BatchProgress;
import com.thbs.progress_tracker.Entity.CourseProgress;
import com.thbs.progress_tracker.Entity.Progress;
import com.thbs.progress_tracker.Entity.ResourceProgress;
import com.thbs.progress_tracker.Entity.TopicProgress;
import com.thbs.progress_tracker.Repository.ProgressRepository;
import com.thbs.progress_tracker.Service.BatchProgressService;

import static org.junit.jupiter.api.Assertions.*;

class BatchProgressServiceTest {

    @InjectMocks
    private BatchProgressService batchProgressService;

    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private RestTemplate restTemplate;

    @Value("${batchModule.uri}")
    String batchModuleUri;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindBatchwiseProgress_WithResults() {
        BatchesDTO batch1 = new BatchesDTO(1L);
        List<BatchesDTO> batches = List.of(batch1);

        when(restTemplate.exchange(batchProgressService.batchModuleUri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<BatchesDTO>>() {
                })).thenReturn(ResponseEntity.ok(batches));
        when(progressRepository.findByBatchesBatchId(1L))
                .thenReturn(List.of(createProgressWithBatchId(1L)));

        List<BatchProgressDTO> result = batchProgressService.findBatchwiseProgress();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getBatchId());
    }

    @Test
    void testFindBatchwiseProgress_NoResults() {
        when(restTemplate.exchange(batchProgressService.batchModuleUri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<BatchesDTO>>() {
                })).thenReturn(ResponseEntity.ok(Collections.emptyList()));

        List<BatchProgressDTO> result = batchProgressService.findBatchwiseProgress();

        assertTrue(result.isEmpty());
    }

    @Test
    void testCalculateBatchProgress_WithResults() {
        when(progressRepository.findByBatchesBatchId(1L))
                .thenReturn(List.of(createProgressWithBatchId(1L, 75.0)));

        BatchProgressDTO result = batchProgressService.calculateBatchProgress(1L);

        assertEquals(1L, result.getBatchId());
        assertEquals(75.0, result.getBatchProgress());
    }

    @Test
    void testCalculateBatchProgress_NoResults() {
        when(progressRepository.findByBatchesBatchId(1L))
                .thenReturn(Collections.emptyList());

        BatchProgressDTO result = batchProgressService.calculateBatchProgress(1L);

        assertEquals(0.0, result.getBatchProgress());
    }

    @Test
    void testCalculateOverallBatchProgressAllUsers_WithResults() {
        when(progressRepository.findByBatchesBatchId(1L))
                .thenReturn(List.of(createProgressWithBatchId(1L, 60.0)));

        List<UserBatchProgressDTO> result = batchProgressService.calculateOverallBatchProgressAllUsers(1L);

        assertEquals(1, result.size());
        assertEquals(60.0, result.get(0).getOverallProgress());
    }

    @Test
    void testCalculateOverallBatchProgressAllUsers_NoResults() {
        when(progressRepository.findByBatchesBatchId(1L))
                .thenReturn(Collections.emptyList());

        List<UserBatchProgressDTO> result = batchProgressService.calculateOverallBatchProgressAllUsers(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCalculateCourseProgressOfUsersInBatch_WithResults() {
        when(progressRepository.findByBatchesBatchId(1L))
                .thenReturn(List.of(createProgressWithCourse(1L, 1L, 100.0)));

        List<UserCourseProgressDTO> result = batchProgressService.calculateCourseProgressOfUsersInBatch(1L, 1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(100.0, result.get(0).getCourseProgress());
    }

    @Test
    void testCalculateCourseProgressOfUsersInBatch_NoResults() {
        when(progressRepository.findByBatchesBatchId(1L))
                .thenReturn(Collections.emptyList());

        List<UserCourseProgressDTO> result = batchProgressService.calculateCourseProgressOfUsersInBatch(1L, 1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetBatchWiseCourseProgress_WithResults() {
        when(progressRepository.findByBatchesBatchId(1L))
                .thenReturn(List.of(createProgressWithCourse(1L, 1L, 90.0)));

        BatchWiseCourseProgressDTO result = batchProgressService.getBatchWiseCourseProgress(1L, 1L);

        assertEquals(1L, result.getBatchId());
        assertEquals(1L, result.getCourseId());
        assertEquals(100.0, result.getCourseProgress());
    }

    @Test
    void testGetBatchWiseCourseProgress_NoResults() {
        when(progressRepository.findByBatchesBatchId(1L))
                .thenReturn(Collections.emptyList());

        BatchWiseCourseProgressDTO result = batchProgressService.getBatchWiseCourseProgress(1L, 1L);

        assertEquals(0.0, result.getCourseProgress());
    }

    @Test
    void testGetBatchWiseTopicProgress_WithResults() {
        when(progressRepository.findByBatchesBatchId(1L))
                .thenReturn(List.of(createProgressWithTopic(1L, 1L, 1L, 70.0)));

        BatchWiseTopicProgressDTO result = batchProgressService.getBatchWiseTopicProgress(1L, 1L, 1L);

        assertEquals(1L, result.getBatchId());
        assertEquals(1L, result.getTopicId());
        assertEquals(70.0, result.getTopicProgress());
    }

    @Test
    void testGetBatchWiseTopicProgress_NoResults() {
        when(progressRepository.findByBatchesBatchId(1L))
                .thenReturn(Collections.emptyList());

        BatchWiseTopicProgressDTO result = batchProgressService.getBatchWiseTopicProgress(1L, 1L, 1L);

        assertEquals(0.0, result.getTopicProgress());
    }

    @Test
    void testGetBatchWiseResourceProgress_WithResults() {
        when(progressRepository.findByBatchesBatchId(1L))
                .thenReturn(List.of(createProgressWithResource(1L, 1L, 1L, 1L, 85.0)));

        BatchWiseResourceProgressDTO result = batchProgressService.getBatchWiseResourceProgress(1L, 1L, 1L, 1L);

        assertEquals(1L, result.getBatchId());
        assertEquals(1L, result.getResourceId());
        assertEquals(85.0, result.getResourceProgress());
    }

    @Test
    void testGetBatchWiseResourceProgress_NoResults() {
        when(progressRepository.findByBatchesBatchId(1L))
                .thenReturn(Collections.emptyList());

        BatchWiseResourceProgressDTO result = batchProgressService.getBatchWiseResourceProgress(1L, 1L, 1L, 1L);

        assertEquals(0.0, result.getResourceProgress());
    }

    // Helper methods to create sample Progress objects
    private Progress createProgressWithBatchId(long batchId) {
        Progress progress = new Progress();
        BatchProgress batchProgress = new BatchProgress(batchId, 100.0, null);
        progress.setBatches(List.of(batchProgress));
        return progress;
    }

    private Progress createProgressWithBatchId(long batchId, double completionPercentage) {
        Progress progress = new Progress();
        BatchProgress batchProgress = new BatchProgress(batchId, completionPercentage, null);
        progress.setBatches(List.of(batchProgress));
        return progress;
    }

    private Progress createProgressWithCourse(long batchId, long courseId, double completionPercentage) {
        Progress progress = new Progress();
        progress.setUserId(1L); // Assign a valid user ID

        ResourceProgress resourceProgress = new ResourceProgress(1L, completionPercentage);
        TopicProgress topicProgress = new TopicProgress(1L, 100.0, List.of(resourceProgress));
        CourseProgress courseProgress = new CourseProgress(courseId, 100.0, List.of(topicProgress));
        BatchProgress batchProgress = new BatchProgress(batchId, 100.0, List.of(courseProgress));

        progress.setBatches(List.of(batchProgress));
        return progress;
    }

    private Progress createProgressWithTopic(long batchId, long courseId, long topicId, double completionPercentage) {
        Progress progress = new Progress();
        TopicProgress topicProgress = new TopicProgress(topicId, completionPercentage, null);
        CourseProgress courseProgress = new CourseProgress(courseId, 100.0, List.of(topicProgress));
        BatchProgress batchProgress = new BatchProgress(batchId, 100.0, List.of(courseProgress));
        progress.setBatches(List.of(batchProgress));
        return progress;
    }

    private Progress createProgressWithResource(long batchId, long courseId, long topicId, long resourceId,
            double completionPercentage) {
        Progress progress = new Progress();
        ResourceProgress resourceProgress = new ResourceProgress(resourceId, completionPercentage);
        TopicProgress topicProgress = new TopicProgress(topicId, 100.0, List.of(resourceProgress));
        CourseProgress courseProgress = new CourseProgress(courseId, 100.0, List.of(topicProgress));
        BatchProgress batchProgress = new BatchProgress(batchId, 100.0, List.of(courseProgress));
        progress.setBatches(List.of(batchProgress));
        return progress;
    }

}
