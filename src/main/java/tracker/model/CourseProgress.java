package tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class CourseProgress {
    public static final CourseProgress DEFAULT = new CourseProgress(0, 0);

    final int earnedPoints;
    final int completedTasks;

    public CourseProgress merge(CourseProgress other) {
        return new CourseProgress(
                earnedPoints + other.earnedPoints,
                completedTasks + other.completedTasks
        );
    }

}
