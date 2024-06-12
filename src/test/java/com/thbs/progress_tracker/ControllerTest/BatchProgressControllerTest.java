package com.thbs.progress_tracker.ControllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.thbs.progress_tracker.Controller.BatchProgressController;
import com.thbs.progress_tracker.DTO.BatchProgressDTO;
import com.thbs.progress_tracker.DTO.BatchWiseCourseProgressDTO;
import com.thbs.progress_tracker.DTO.BatchWiseResourceProgressDTO;
import com.thbs.progress_tracker.DTO.BatchWiseTopicProgressDTO;
import com.thbs.progress_tracker.DTO.UserBatchProgressDTO;
import com.thbs.progress_tracker.DTO.UserCourseProgressDTO;
import com.thbs.progress_tracker.Service.BatchProgressService;

import java.util.Collections;
import java.util.List;

@WebMvcTest(BatchProgressController.class)
public class BatchProgressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BatchProgressService batchProgressService;

    @Test
    void testCalculateBatchwiseProgress_WithResults() throws Exception {
        List<BatchProgressDTO> progressList = List.of(new BatchProgressDTO());
        when(batchProgressService.findBatchwiseProgress()).thenReturn(progressList);

        mockMvc.perform(get("/batch-progress")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]")); // Match the JSON structure
    }

    @Test
    void testCalculateBatchwiseProgress_NoResults() throws Exception {
        when(batchProgressService.findBatchwiseProgress()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/batch-progress")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No batches found."));
    }

    @Test
    void testCalculateBatchProgress_WithResult() throws Exception {
        BatchProgressDTO progress = new BatchProgressDTO();
        when(batchProgressService.calculateBatchProgress(1)).thenReturn(progress);

        mockMvc.perform(get("/batch-progress/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}")); // Match the JSON structure
    }

    @Test
    void testCalculateBatchProgress_NoResult() throws Exception {
        when(batchProgressService.calculateBatchProgress(1)).thenReturn(null);

        mockMvc.perform(get("/batch-progress/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOverallBatchProgress_WithResults() throws Exception {
        List<UserBatchProgressDTO> progressList = List.of(new UserBatchProgressDTO());
        when(batchProgressService.calculateOverallBatchProgressAllUsers(1L)).thenReturn(progressList);

        mockMvc.perform(get("/batch-progress/allusers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"));
    }

    @Test
    void testGetOverallBatchProgress_NoResults() throws Exception {
        when(batchProgressService.calculateOverallBatchProgressAllUsers(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/batch-progress/allusers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCourseProgressOfUsersInBatch_WithResults() throws Exception {
        List<UserCourseProgressDTO> progressList = List.of(new UserCourseProgressDTO());
        when(batchProgressService.calculateCourseProgressOfUsersInBatch(1L, 1L)).thenReturn(progressList);

        mockMvc.perform(get("/batch-progress/all-users/1/course/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}]"));
    }

    @Test
    void testGetCourseProgressOfUsersInBatch_NoResults() throws Exception {
        when(batchProgressService.calculateCourseProgressOfUsersInBatch(1L, 1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/batch-progress/all-users/1/course/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOverallBatchWiseCourseProgress_WithResult() throws Exception {
        BatchWiseCourseProgressDTO progress = new BatchWiseCourseProgressDTO();
        when(batchProgressService.getBatchWiseCourseProgress(1L, 1L)).thenReturn(progress);

        mockMvc.perform(get("/batch-progress/batch-course-progress/batch/1/course/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void testGetOverallBatchWiseCourseProgress_NoResult() throws Exception {
        when(batchProgressService.getBatchWiseCourseProgress(1L, 1L)).thenReturn(null);

        mockMvc.perform(get("/batch-progress/batch-course-progress/batch/1/course/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOverallBatchWiseTopicProgress_WithResult() throws Exception {
        BatchWiseTopicProgressDTO progress = new BatchWiseTopicProgressDTO();
        when(batchProgressService.getBatchWiseTopicProgress(1L, 1L, 1L)).thenReturn(progress);

        mockMvc.perform(get("/batch-progress/batch-topic-progress/batch/1/course/1/topic/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void testGetOverallBatchWiseTopicProgress_NoResult() throws Exception {
        when(batchProgressService.getBatchWiseTopicProgress(1L, 1L, 1L)).thenReturn(null);

        mockMvc.perform(get("/batch-progress/batch-topic-progress/batch/1/course/1/topic/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOverallBatchWiseResourceProgress_WithResult() throws Exception {
        BatchWiseResourceProgressDTO progress = new BatchWiseResourceProgressDTO();
        when(batchProgressService.getBatchWiseResourceProgress(1L, 1L, 1L, 1L)).thenReturn(progress);

        mockMvc.perform(get("/batch-progress/batch-resource-progress/batch/1/course/1/topic/1/resource/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOverallBatchWiseResourceProgress_NoResult() throws Exception {
        when(batchProgressService.getBatchWiseResourceProgress(1L, 1L, 1L, 1L)).thenReturn(null);

        mockMvc.perform(get("/batch-progress/batch-resource-progress/batch/1/course/1/topic/1/resource/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}