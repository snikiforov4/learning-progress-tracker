package tracker.statistics;

import tracker.model.Course;
import tracker.statistics.model.CourseStatistic;
import tracker.statistics.model.OverallStatistic;

public interface IStatisticsService {
    OverallStatistic getOverallStatistic();

    CourseStatistic getCourseStatistic(Course course);
}
