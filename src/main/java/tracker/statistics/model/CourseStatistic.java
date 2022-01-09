package tracker.statistics.model;

import tracker.model.Course;

import java.util.List;

public record CourseStatistic(Course course, List<CourseUserStats> userStats) {
}
