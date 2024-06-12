package com.thbs.progress_tracker.ControllerTest;


import com.thbs.progress_tracker.Controller.UserProgressController;
import com.thbs.progress_tracker.DTO.*;
import com.thbs.progress_tracker.Entity.Progress;
import com.thbs.progress_tracker.Service.UserProgressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserProgressControllerTest {

    @InjectMocks
    private UserProgressController userProgressController;

    @Mock
    private UserProgressService progressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateProgress() {
        doNothing().when(progressService).updateProgress(anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyDouble());

        userProgressController.updateProgress(1L, 1L, 1L, 1L, 1L, 50.0);

        verify(progressService, times(1)).updateProgress(1L, 1L, 1L, 1L, 1L, 50.0);
    }

    @Test
    void testGetProgress() {
        Progress progress = new Progress();
        when(progressService.getProgressByUserId(1L)).thenReturn(Optional.of(progress));

        ResponseEntity<?> responseEntity = userProgressController.getProgress(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(progress, responseEntity.getBody());

        when(progressService.getProgressByUserId(2L)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntityNoContent = userProgressController.getProgress(2L);
        assertEquals(HttpStatus.NO_CONTENT, responseEntityNoContent.getStatusCode());
        assertEquals("No data found", responseEntityNoContent.getBody());
    }

    @Test
    void testGetUserOverallProgress() {
        double progress = 75.0;
        when(progressService.calculateUserOverallProgress(1L, 1L)).thenReturn(progress);

        UserOverallProgressDTO expectedResponse = new UserOverallProgressDTO(1L, 1L, progress);
        ResponseEntity<UserOverallProgressDTO> responseEntity = userProgressController.getUserOverallProgress(1L, 1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetUserCourseOverallProgress() {
        double progress = 80.0;
        when(progressService.calculateUserCourseOverallProgress(1L, 1L, 1L)).thenReturn(progress);

        CourseOverallProgressDTO expectedResponse = new CourseOverallProgressDTO(1L, 1L, 1L, progress);
        ResponseEntity<CourseOverallProgressDTO> responseEntity = userProgressController.getUserCourseOverallProgress(1L, 1L, 1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetUserCourseTopicOverallProgress() {
        double progress = 85.0;
        when(progressService.calculateUserCourseTopicOverallProgress(1L, 1L, 1L, 1L)).thenReturn(progress);

        UserCourseTopicOverallProgressDTO expectedResponse = new UserCourseTopicOverallProgressDTO(1L, 1L, 1L, 1L, progress);
        ResponseEntity<UserCourseTopicOverallProgressDTO> responseEntity = userProgressController.getUserCourseTopicOverallProgress(1L, 1L, 1L, 1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testGetUserCourseTopicResourceOverallProgress() {
        double progress = 90.0;
        when(progressService.calculateUserCourseTopicResourceOverallProgress(1L, 1L, 1L, 1L, 1L)).thenReturn(progress);

        UserCourseTopicResourceOverallProgressDTO expectedResponse = new UserCourseTopicResourceOverallProgressDTO(1L, 1L, 1L, 1L, 1L, progress);
        ResponseEntity<UserCourseTopicResourceOverallProgressDTO> responseEntity = userProgressController.getUserCourseTopicResourceOverallProgress(1L, 1L, 1L, 1L, 1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testDeleteProgressForUsers() {
        DeleteProgressOfUsersDTO deleteUsers = new DeleteProgressOfUsersDTO();
        deleteUsers.setBatchId(1L);
        deleteUsers.setUserIds(List.of(1L, 2L));

        doNothing().when(progressService).deleteProgressOfUsers(anyList(), anyLong());

        ResponseEntity<?> responseEntity = userProgressController.deleteProgressForUsers(deleteUsers);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(progressService, times(1)).deleteProgressOfUsers(deleteUsers.getUserIds(), deleteUsers.getBatchId());
    }
}
