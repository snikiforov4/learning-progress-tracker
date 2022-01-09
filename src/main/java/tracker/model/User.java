package tracker.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@ToString
public final class User {

    private final EnumMap<Course, CourseProgress> courseProgresses;
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String firstName;
    @Getter
    @Setter
    private String lastName;

    public static User fromCredentials(UserCredentials credentials) {
        User user = new User();
        user.setFirstName(credentials.getFirstName());
        user.setLastName(credentials.getLastName());
        user.setEmail(credentials.getEmail());
        return user;
    }

    public User() {
        this.courseProgresses = new EnumMap<>(Course.class);
    }

    public void completeTaskByCourse(Course course, int points) {
        Objects.requireNonNull(course);
        courseProgresses.merge(course, new CourseProgress(points, 1), CourseProgress::merge);
    }

    public CourseProgress getCourseProgress(Course course) {
        return courseProgresses.getOrDefault(course, CourseProgress.DEFAULT);
    }

    public Map<Course, CourseProgress> getAllCourseProgresses() {
        return Collections.unmodifiableMap(courseProgresses);
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
