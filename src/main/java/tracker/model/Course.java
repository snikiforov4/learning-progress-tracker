package tracker.model;

import java.util.HashMap;
import java.util.Map;

public enum Course {
    JAVA("Java", 600),
    DATA_STRUCTURES_AND_ALGORITHMS("DSA", 400),
    DATABASES("Databases", 480),
    SPRING("Spring", 550);

    private static final Map<String, Course> COURSES = new HashMap<>();

    static {
        for (Course course : Course.values()) {
            COURSES.put(course.name, course);
        }
    }

    public static Course getCourseByName(String name) {
        return COURSES.get(name);
    }

    private final String name;
    private final int pointsToFinish;

    Course(String name, int pointsToFinish) {
        this.name = name;
        this.pointsToFinish = pointsToFinish;
    }

    public String getName() {
        return name;
    }

    public int getPointsToFinish() {
        return pointsToFinish;
    }
}
