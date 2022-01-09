package tracker.statistics.model;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import tracker.model.Course;

import java.util.List;
import java.util.Objects;

public final class OverallStatistic {

    private final Multimap<Category, Course> stats;

    public OverallStatistic(Multimap<Category, Course> stats) {
        this.stats = ImmutableMultimap.copyOf(stats.entries().stream().filter(entry -> Objects.nonNull(entry.getValue())).toList());
    }

    public List<Course> by(Category category) {
        return (List<Course>) stats.get(category);
    }
}
