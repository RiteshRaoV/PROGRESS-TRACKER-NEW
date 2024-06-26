package com.thbs.progress_tracker.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.thbs.progress_tracker.Config.RoundConfig;
import com.thbs.progress_tracker.DTO.BatchCoursesDTO;
import com.thbs.progress_tracker.DTO.BatchResourceDTO;
import com.thbs.progress_tracker.DTO.BatchTopicDTO;
import com.thbs.progress_tracker.DTO.BatchesDTO;
import com.thbs.progress_tracker.DTO.LearningPlanDTO;
import com.thbs.progress_tracker.Entity.BatchProgress;
import com.thbs.progress_tracker.Entity.CourseProgress;
import com.thbs.progress_tracker.Entity.Progress;
import com.thbs.progress_tracker.Entity.ResourceProgress;
import com.thbs.progress_tracker.Entity.TopicProgress;
import com.thbs.progress_tracker.Repository.ProgressRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserProgressService {

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RoundConfig roundConfig;

    @Value("${learningPlanModule.url}")
    String learningPlanModuleUri;

    @Value("${learningResourceModule.uri}")
    String learningResourceModuleUri;

    public void updateProgress(Long userId, Long batchId, Long courseId, Long topicId, Long resourceId,
            double completionPercentage) {
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
                    BatchProgress newBatchProgress = new BatchProgress(batchId, 0.0, new ArrayList<>());
                    progress.getBatches().add(newBatchProgress);
                    return newBatchProgress;
                });
    }

    private CourseProgress findOrCreateCourseProgress(BatchProgress batchProgress, Long courseId) {
        return batchProgress.getCourses().stream()
                .filter(cp -> cp.getCourseId().equals(courseId))
                .findFirst()
                .orElseGet(() -> {
                    CourseProgress newCourseProgress = new CourseProgress(courseId, 0.0, new ArrayList<>());
                    batchProgress.getCourses().add(newCourseProgress);
                    return newCourseProgress;
                });
    }

    private TopicProgress findOrCreateTopicProgress(CourseProgress courseProgress, Long topicId) {
        return courseProgress.getTopics().stream()
                .filter(tp -> tp.getTopicId().equals(topicId))
                .findFirst()
                .orElseGet(() -> {
                    TopicProgress newTopicProgress = new TopicProgress(topicId, 0.0, new ArrayList<>());
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

        resourceProgress.setCompletionPercentage(roundConfig.roundToTwoDecimalPlaces(completionPercentage));
    }

    private void updateTopicProgress(TopicProgress topicProgress) {
        double averageCompletion = topicProgress.getResources().stream()
                .mapToDouble(ResourceProgress::getCompletionPercentage)
                .average()
                .orElse(0.0);

        topicProgress.setCompletionPercentage(roundConfig.roundToTwoDecimalPlaces(averageCompletion));
    }

    private void updateCourseProgress(CourseProgress courseProgress) {
        double averageCompletion = courseProgress.getTopics().stream()
                .mapToDouble(TopicProgress::getCompletionPercentage)
                .average()
                .orElse(0.0);

        courseProgress.setCompletionPercentage(roundConfig.roundToTwoDecimalPlaces(averageCompletion));
    }

    private void updateOverallProgress(BatchProgress batchProgress) {
        double averageCompletion = batchProgress.getCourses().stream()
                .mapToDouble(CourseProgress::getCompletionPercentage)
                .average()
                .orElse(0.0);

        batchProgress.setOverallCompletionPercentage(roundConfig.roundToTwoDecimalPlaces(averageCompletion));
    }

    public double calculateUserOverallProgress(Long userId, Long batchId) {
        Optional<Progress> optionalProgress = progressRepository.findByUserId(userId);
        if (optionalProgress.isEmpty()) {
            return 0;
        }
        Progress progress = optionalProgress.get();
        Optional<BatchProgress> batchProgressOptional = progress.getBatches().stream()
                .filter(batch -> batch.getBatchId().equals(batchId))
                .findFirst();
        if (batchProgressOptional.isEmpty()) {
            return 0;
        }
        BatchProgress batchProgress = batchProgressOptional.get();
        double totalCompletion = batchProgress.getCourses().stream()
                .mapToDouble(CourseProgress::getCompletionPercentage)
                .sum();
        int totalCourses = batchProgress.getCourses().size();
        return totalCourses > 0 ? totalCompletion / totalCourses : 0;
    }

    public double calculateUserCourseOverallProgress(Long userId, Long batchId, Long courseId) {
        Optional<Progress> optionalProgress = progressRepository.findByUserId(userId);

        if (optionalProgress.isEmpty()) {
            return 0;
        }

        Progress progress = optionalProgress.get();

        Optional<CourseProgress> courseProgressOptional = progress.getBatches().stream()
                .filter(batch -> batch.getBatchId().equals(batchId))
                .flatMap(batch -> batch.getCourses().stream())
                .filter(course -> course.getCourseId().equals(courseId))
                .findFirst();

        if (courseProgressOptional.isEmpty()) {
            return 0;
        }

        CourseProgress courseProgress = courseProgressOptional.get();

        double totalCompletion = courseProgress.getTopics().stream()
                .mapToDouble(TopicProgress::getCompletionPercentage)
                .sum();

        int totalTopics = courseProgress.getTopics().size();

        return totalTopics > 0 ? totalCompletion / totalTopics : 0;
    }

    public double calculateUserCourseTopicOverallProgress(Long userId, Long batchId, Long courseId, Long topicId) {
        Optional<Progress> optionalProgress = progressRepository.findByUserId(userId);

        if (optionalProgress.isEmpty()) {
            return 0;
        }

        Progress progress = optionalProgress.get();

        Optional<TopicProgress> topicProgressOptional = progress.getBatches().stream()
                .filter(batch -> batch.getBatchId().equals(batchId))
                .flatMap(batch -> batch.getCourses().stream())
                .filter(course -> course.getCourseId().equals(courseId))
                .flatMap(course -> course.getTopics().stream())
                .filter(topic -> topic.getTopicId().equals(topicId))
                .findFirst();

        if (topicProgressOptional.isEmpty()) {
            return 0;
        }

        TopicProgress topicProgress = topicProgressOptional.get();

        double totalCompletion = topicProgress.getResources().stream()
                .mapToDouble(ResourceProgress::getCompletionPercentage)
                .sum();

        int totalResources = topicProgress.getResources().size();

        return totalResources > 0 ? totalCompletion / totalResources : 0;
    }

    public double calculateUserCourseTopicResourceOverallProgress(Long userId, Long batchId, Long courseId,
            Long topicId, Long resourceId) {
        Optional<Progress> optionalProgress = progressRepository.findByUserId(userId);

        if (optionalProgress.isEmpty()) {
            return 0;
        }

        Progress progress = optionalProgress.get();

        Optional<ResourceProgress> resourceProgressOptional = progress.getBatches().stream()
                .filter(batch -> batch.getBatchId().equals(batchId))
                .flatMap(batch -> batch.getCourses().stream())
                .filter(course -> course.getCourseId().equals(courseId))
                .flatMap(course -> course.getTopics().stream())
                .filter(topic -> topic.getTopicId().equals(topicId))
                .flatMap(topic -> topic.getResources().stream())
                .filter(resource -> resource.getResourceId().equals(resourceId))
                .findFirst();

        if (resourceProgressOptional.isEmpty()) {
            return 0;
        }

        return resourceProgressOptional.get().getCompletionPercentage();
    }

    public void deleteProgressOfUsers(List<Long> userIds, Long batchId) {
        List<Progress> progresses = progressRepository.findByUserIdIn(userIds);
        progresses.forEach(progress -> progress.getBatches().removeIf(batch -> batch.getBatchId().equals(batchId)));
        progressRepository.saveAll(progresses);
    }

    public void setProgressForNewUsers(List<Long> userIds, long batchId) {
        List<Progress> existingProgresses = progressRepository.findByUserIdIn(userIds);
        Map<Long, Progress> userProgressMap = existingProgresses.stream()
                .collect(Collectors.toMap(Progress::getUserId, Function.identity()));

        String uri = learningPlanModuleUri;
        ResponseEntity<LearningPlanDTO> response = restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<LearningPlanDTO>() {
                }, batchId);
        LearningPlanDTO learningPlan = response.getBody();

        List<Long> courseIds = learningPlan.getBatchCourses().stream()
                .map(BatchCoursesDTO::getCourseId) 
                .collect(Collectors.toList());

        List<Long> topicIds = learningPlan.getBatchCourses().stream() 
                .flatMap(batchCourse -> batchCourse.getTopic().stream()) 
                .map(BatchTopicDTO::getTopicId) 
                .collect(Collectors.toList());        
        
        String baseUrl = learningResourceModuleUri;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("batchId", batchId);

        for (Long courseId : courseIds) {
            builder.queryParam("courseIds", courseId);
        }

        for (Long topicId : topicIds) {
            builder.queryParam("topicIds", topicId);
        }

        URI learningResourceUri = builder.build().encode().toUri();

        ResponseEntity<LearningPlanDTO> finalResponse = restTemplate.getForEntity(learningResourceUri, LearningPlanDTO.class);

        LearningPlanDTO learningPlanWithResources = finalResponse.getBody();

        userIds.forEach(userId -> {
            Progress progress = userProgressMap.get(userId);

            if (progress == null) {
                progress = new Progress();
                progress.setUserId(userId);
                progress.setBatches(new ArrayList<>());
            }

            boolean batchExists = progress.getBatches().stream().anyMatch(bp -> bp.getBatchId().equals(batchId));
            if (!batchExists) {
                BatchProgress batchProgress = new BatchProgress();
                batchProgress.setBatchId(batchId);
                batchProgress.setOverallCompletionPercentage(0);

                List<CourseProgress> courses = new ArrayList<>();
                for (BatchCoursesDTO course : learningPlanWithResources.getBatchCourses()) {
                    CourseProgress courseProgress = new CourseProgress();
                    courseProgress.setCourseId(course.getCourseId());
                    courseProgress.setCompletionPercentage(0);

                    List<TopicProgress> topics = new ArrayList<>();
                    for (BatchTopicDTO topic : course.getTopic()) {
                        TopicProgress topicProgress = new TopicProgress();
                        topicProgress.setTopicId(topic.getTopicId());
                        topicProgress.setCompletionPercentage(0);
                        List<ResourceProgress> resources = new ArrayList<>();
                        for (BatchResourceDTO resource:topic.getBatchResourses()){
                            ResourceProgress resourceProgress = new ResourceProgress();
                            resourceProgress.setResourceId(resource.getResourceId());
                            resourceProgress.setCompletionPercentage(0);
                            resources.add(resourceProgress);
                        }
                        topicProgress.setResources(resources); 
                        topics.add(topicProgress);
                    }

                    courseProgress.setTopics(topics);
                    courses.add(courseProgress);
                }

                batchProgress.setCourses(courses);
                progress.getBatches().add(batchProgress);
            }

            progressRepository.save(progress);
        });
    }

    public LearningPlanDTO getBatchLearningPlan(long batchId) {
        String uri = learningPlanModuleUri;
        ResponseEntity<LearningPlanDTO> response = restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<LearningPlanDTO>() {
                }, batchId);
        return response.getBody();
    }
}
