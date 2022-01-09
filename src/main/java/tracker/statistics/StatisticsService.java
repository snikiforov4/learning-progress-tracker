package tracker.statistics;

import com.google.common.collect.*;
import lombok.AllArgsConstructor;
import tracker.model.Course;
import tracker.model.CourseProgress;
import tracker.model.User;
import tracker.repository.IUserRepository;
import tracker.statistics.model.Category;
import tracker.statistics.model.CourseStatistic;
import tracker.statistics.model.CourseUserStats;
import tracker.statistics.model.OverallStatistic;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@AllArgsConstructor
public class StatisticsService implements IStatisticsService {

    private final IUserRepository userRepository;

    private static <V extends Comparable<? super V>> MinMax findMinMax(
            Map<Course, V> map, Comparator<Map.Entry<Course, V>> comparator) {
        List<Map.Entry<Course, V>> entries = map.entrySet().stream().sorted(comparator).toList();
        V prevValue = null;
        Collection<Course> max = new HashSet<>();
        for (ListIterator<Map.Entry<Course, V>> iterator = entries.listIterator(entries.size()); iterator.hasPrevious(); ) {
            Map.Entry<Course, V> entry = iterator.previous();
            V curValue = entry.getValue();
            if (prevValue != null && !Objects.equals(curValue, prevValue)) {
                break;
            }
            max.add(entry.getKey());
            prevValue = curValue;
        }
        prevValue = null;
        Collection<Course> min = new HashSet<>();
        for (var entry : entries) {
            V curValue = entry.getValue();
            if (max.contains(entry.getKey())) {
                break;
            }
            if (prevValue != null && !Objects.equals(curValue, prevValue)) {
                break;
            }
            min.add(entry.getKey());
            prevValue = curValue;
        }
        return new MinMax(min, max);
    }

    @Override
    public OverallStatistic getOverallStatistic() {
        List<User> allUsers = userRepository.getAll();
        MinMax byPopularity = byPopularity(allUsers);
        MinMax byActivity = byActivity(allUsers);
        MinMax byDifficulty = byDifficulty(allUsers);
        Multimap<Category, Course> result = HashMultimap.create(Category.values().length, 1);
        byActivity.min().forEach(v -> result.put(Category.LOWEST_ACTIVITY, v));
        byActivity.max().forEach(v -> result.put(Category.HIGHEST_ACTIVITY, v));
        byPopularity.min().forEach(v -> result.put(Category.LEAST_POPULAR, v));
        byPopularity.max().forEach(v -> result.put(Category.MOST_POPULAR, v));
        byDifficulty.min().forEach(v -> result.put(Category.EASIEST_COURSE, v));
        byDifficulty.max().forEach(v -> result.put(Category.HARDEST_COURSE, v));
        return new OverallStatistic(result);
    }

    @Override
    public CourseStatistic getCourseStatistic(Course course) {
        List<User> allUsers = userRepository.getAll();
        List<CourseUserStats> list = Lists.newArrayList();
        for (User user : allUsers) {
            CourseProgress progress = user.getCourseProgress(course);
            if (progress.getEarnedPoints() > 0) {
                list.add(new CourseUserStats(user.getId(), progress.getEarnedPoints()));
            }
        }
        list.sort(Comparator.comparingInt(CourseUserStats::getPoints).reversed().thenComparing(CourseUserStats::getId));
        return new CourseStatistic(course, list);
    }

    private MinMax byPopularity(List<User> allUsers) {
        Map<Course, Long> grouping = allUsers.stream()
                .flatMap((User u) -> u.getAllCourseProgresses().keySet().stream())
                .collect(groupingBy(Function.identity(), counting()));
        return findMinMax(grouping, Map.Entry.comparingByValue());
    }

    private MinMax byActivity(List<User> allUsers) {
        Map<Course, Long> grouping = allUsers.stream()
                .flatMap((User u) -> u.getAllCourseProgresses().entrySet().stream())
                .collect(groupingBy(Map.Entry::getKey, summingLong(e -> e.getValue().getCompletedTasks())));
        return findMinMax(grouping, Map.Entry.comparingByValue());
    }

    private MinMax byDifficulty(List<User> allUsers) {
        Multimap<Course, Double> earnedPoints = ArrayListMultimap.create();
        for (User user : allUsers) {
            user.getAllCourseProgresses().forEach(((course, progress) ->
                    earnedPoints.put(course, (double) progress.getEarnedPoints() / progress.getCompletedTasks())));
        }
        Map<Course, Double> average = Maps.transformValues(earnedPoints.asMap(), this::average);
        return findMinMax(average, Map.Entry.comparingByValue(Comparator.reverseOrder()));
    }

    private Double average(Collection<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private record MinMax(Iterable<Course> min, Iterable<Course> max) {
    }

}
