package com.thbs.progress_tracker.ServiceTest;

import com.thbs.progress_tracker.Entity.*;
import com.thbs.progress_tracker.Repository.ProgressRepository;
import com.thbs.progress_tracker.Service.UserProgressService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserProgressServiceTest {

    @InjectMocks
    private UserProgressService userProgressService;

    @Mock
    private ProgressRepository progressRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateProgress() {
        Progress progress = new Progress(1L, new ArrayList<>());
        when(progressRepository.findByUserId(1L)).thenReturn(Optional.of(progress));
        when(progressRepository.save(progress)).thenReturn(progress);

        userProgressService.updateProgress(1L, 1L, 1L, 1L, 1L, 50.0);

        verify(progressRepository, times(1)).save(progress);
    }

    @Test
    void testGetProgressByUserId() {
        Progress progress = new Progress(1L, new ArrayList<>());
        when(progressRepository.findByUserId(1L)).thenReturn(Optional.of(progress));

        Optional<Progress> result = userProgressService.getProgressByUserId(1L);
        assertEquals(progress, result.orElse(null));
    }

    @Test
    void testCalculateUserOverallProgress() {
        List<BatchProgress> batchProgresses = new ArrayList<>();
        List<CourseProgress> courseProgresses = new ArrayList<>();
        courseProgresses.add(new CourseProgress(1L, 80.0, new ArrayList<>()));
        batchProgresses.add(new BatchProgress(1L, 0.0, courseProgresses));
        Progress progress = new Progress(1L, batchProgresses);

        when(progressRepository.findByUserId(1L)).thenReturn(Optional.of(progress));

        double overallProgress = userProgressService.calculateUserOverallProgress(1L, 1L);
        assertEquals(80.0, overallProgress);
    }

    @Test
    void testCalculateUserCourseOverallProgress() {
        List<TopicProgress> topicProgresses = new ArrayList<>();
        topicProgresses.add(new TopicProgress(1L, 90.0, new ArrayList<>()));
        List<CourseProgress> courseProgresses = new ArrayList<>();
        courseProgresses.add(new CourseProgress(1L, 0.0, topicProgresses));
        List<BatchProgress> batchProgresses = new ArrayList<>();
        batchProgresses.add(new BatchProgress(1L, 0.0, courseProgresses));
        Progress progress = new Progress(1L, batchProgresses);

        when(progressRepository.findByUserId(1L)).thenReturn(Optional.of(progress));

        double courseOverallProgress = userProgressService.calculateUserCourseOverallProgress(1L, 1L, 1L);
        assertEquals(90.0, courseOverallProgress);
    }

    @Test
    void testCalculateUserCourseTopicOverallProgress() {
        List<ResourceProgress> resourceProgresses = new ArrayList<>();
        resourceProgresses.add(new ResourceProgress(1L, 70.0));
        List<TopicProgress> topicProgresses = new ArrayList<>();
        topicProgresses.add(new TopicProgress(1L, 0.0, resourceProgresses));
        List<CourseProgress> courseProgresses = new ArrayList<>();
        courseProgresses.add(new CourseProgress(1L, 0.0, topicProgresses));
        List<BatchProgress> batchProgresses = new ArrayList<>();
        batchProgresses.add(new BatchProgress(1L, 0.0, courseProgresses));
        Progress progress = new Progress(1L, batchProgresses);

        when(progressRepository.findByUserId(1L)).thenReturn(Optional.of(progress));

        double topicOverallProgress = userProgressService.calculateUserCourseTopicOverallProgress(1L, 1L, 1L, 1L);
        assertEquals(70.0, topicOverallProgress);
    }

    @Test
    void testCalculateUserCourseTopicResourceOverallProgress() {
        List<ResourceProgress> resourceProgresses = new ArrayList<>();
        resourceProgresses.add(new ResourceProgress(1L, 50.0));
        List<TopicProgress> topicProgresses = new ArrayList<>();
        topicProgresses.add(new TopicProgress(1L, 0.0, resourceProgresses));
        List<CourseProgress> courseProgresses = new ArrayList<>();
        courseProgresses.add(new CourseProgress(1L, 0.0, topicProgresses));
        List<BatchProgress> batchProgresses = new ArrayList<>();
        batchProgresses.add(new BatchProgress(1L, 0.0, courseProgresses));
        Progress progress = new Progress(1L, batchProgresses);

        when(progressRepository.findByUserId(1L)).thenReturn(Optional.of(progress));

        double resourceOverallProgress = userProgressService.calculateUserCourseTopicResourceOverallProgress(1L, 1L, 1L, 1L, 1L);
        assertEquals(50.0, resourceOverallProgress);
    }

    @Test
    void testDeleteProgressOfUsers() {
        Progress progress = new Progress(1L, new ArrayList<>());
        when(progressRepository.findByUserId(1L)).thenReturn(Optional.of(progress));

        userProgressService.deleteProgressOfUsers(List.of(1L), 1L);

        verify(progressRepository, times(1)).save(progress);
    }
}
