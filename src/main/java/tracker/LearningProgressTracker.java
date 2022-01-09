package tracker;

import lombok.NoArgsConstructor;
import tracker.model.Course;
import tracker.model.User;
import tracker.notify.INotificationService;
import tracker.notify.NotificationService;
import tracker.parser.IUserCredentialsParser;
import tracker.parser.UserCredentialsParser;
import tracker.repository.IUserRepository;
import tracker.repository.InMemoryUserRepository;
import tracker.statistics.IStatisticsService;
import tracker.statistics.StatisticsService;
import tracker.statistics.model.Category;
import tracker.statistics.model.CourseStatistic;
import tracker.statistics.model.CourseUserStats;
import tracker.statistics.model.OverallStatistic;
import tracker.userinterface.CommandLineInterface;
import tracker.userinterface.IUserInterface;
import tracker.validate.IUserCredentialsValidator;
import tracker.validate.UserCredentialsValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@NoArgsConstructor
public class LearningProgressTracker {

    private static final Course[] COURSES_SEQUENCE = {Course.JAVA, Course.DATA_STRUCTURES_AND_ALGORITHMS,
            Course.DATABASES, Course.SPRING};

    private final IUserCredentialsParser credentialsParser = new UserCredentialsParser();
    private final IUserCredentialsValidator credentialsValidator = new UserCredentialsValidator();
    private final IUserInterface cli = new CommandLineInterface();
    private final IUserRepository userRepository = new InMemoryUserRepository();
    private final IStatisticsService statisticService = new StatisticsService(userRepository);
    private final INotificationService notificationService = new NotificationService(cli);

    public void run() {
        cli.send("Learning Progress Tracker");
        String userInput;
        infiniteLoop:
        while (true) {
            userInput = cli.receiveUserInput();
            switch (userInput) {
                case "add students":
                    addUsersCycle();
                    break;
                case "add points":
                    addPointsCycle();
                    break;
                case "list":
                    printListOfStudents();
                    break;
                case "find":
                    findStudent();
                    break;
                case "statistics":
                    printStatistics();
                    break;
                case "notify":
                    notificationService.sendNotifications();
                    break;
                case "exit":
                    cli.send("Bye!");
                    break infiniteLoop;
                case "cheat":
                    AtomicInteger stub = new AtomicInteger();
                    tryAddingNewUser("Trixie Winer address1@mail.com", stub);
                    tryAddingNewUser("Leola Whelan address2@mail.com", stub);
                    tryAddingNewUser("Barbara-Anne Chicky address3@mail.com", stub);
                    tryAddingNewUser("Jermaine Naaman address4@mail.com", stub);
                    tryUpdateStudentPoints("1 10 10 10 10");
                    tryUpdateStudentPoints("2 5 5 5 5");
                    tryUpdateStudentPoints("3 5 5 5 5");
                    tryUpdateStudentPoints("4 2 2 2 2");
                    System.out.println(userRepository.getAll());
                case "back":
                    cli.send("Enter 'exit' to exit the program.");
                    break;
                case "":
                    cli.send("No input.");
                    break;
                default:
                    cli.send("Error: unknown command!");
                    break;
            }
        }
    }

    private void addUsersCycle() {
        String userInput;
        cli.send("Enter student credentials or 'back' to return:");
        AtomicInteger addedUsersCnt = new AtomicInteger(0);
        while (true) {
            userInput = cli.receiveUserInput();
            if ("back".equals(userInput)) {
                cli.send(String.format("Total %d students have been added.", addedUsersCnt.get()));
                break;
            } else {
                String response = tryAddingNewUser(userInput, addedUsersCnt);
                cli.send(response);
            }
        }
    }

    private String tryAddingNewUser(String userInput, AtomicInteger addedUsersCnt) {
        var userCredentials = credentialsParser.parseFromString(userInput);
        var validationResult = credentialsValidator.validate(userCredentials);
        return switch (validationResult.getStatus()) {
            case OK -> {
                if (userRepository.existsByEmail(userCredentials.getEmail())) {
                    yield "This email is already taken.";
                }
                User user = User.fromCredentials(userCredentials);
                userRepository.save(user);
                addedUsersCnt.incrementAndGet();
                yield "The student has been added.";
            }
            case INVALID_CREDENTIALS -> "Incorrect credentials.";
            case INVALID_FIRST_NAME -> "Incorrect first name.";
            case INVALID_LAST_NAME -> "Incorrect last name";
            case INVALID_EMAIL -> "Incorrect email";
        };
    }

    private void addPointsCycle() {
        String userInput;
        cli.send("Enter an id and points or 'back' to return:");
        while (true) {
            userInput = cli.receiveUserInput();
            if ("back".equals(userInput)) {
                break;
            } else {
                String response = tryUpdateStudentPoints(userInput);
                cli.send(response);
            }
        }
    }

    private String tryUpdateStudentPoints(String userInput) {
        String[] tokens = userInput.split("\\s+");
        if (tokens.length != 5) {
            return "Incorrect points format.";
        }
        String userId = tokens[0];
        int[] points = new int[4];
        for (int i = 0; i < 4; i++) {
            try {
                int coursePoints = Integer.parseInt(tokens[i + 1]);
                if (coursePoints < 0) {
                    return "Incorrect points format.";
                }
                points[i] = coursePoints;
            } catch (Exception e) {
                return "Incorrect points format.";
            }
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "No student is found for id=" + userId;
        }
        for (int i = 0; i < points.length; i++) {
            Course course = COURSES_SEQUENCE[i];
            final int earnedPointsOldValue = user.getCourseProgress(course).getEarnedPoints();
            user.completeTaskByCourse(course, points[i]);
            final int pointsToFinish = course.getPointsToFinish();
            if (earnedPointsOldValue < pointsToFinish
                    && user.getCourseProgress(course).getEarnedPoints() >= pointsToFinish) {
                notificationService.saveNotification(user, course);
            }
        }
        return "Points updated.";
    }

    private void printListOfStudents() {
        List<User> users = userRepository.getAll();
        if (users.isEmpty()) {
            cli.send("No students found.");
        } else {
            cli.send("Students:");
            users.forEach(u -> cli.send(u.getId()));
        }
    }

    private void findStudent() {
        cli.send("Enter an id or 'back' to return:");
        while (true) {
            String userInput = cli.receiveUserInput();
            if ("back".equals(userInput)) {
                break;
            } else {
                User user = userRepository.findById(userInput).orElse(null);
                if (user == null) {
                    cli.send("No student is found for id=" + userInput);
                    return;
                }
                String response = String.format("%s points: Java=%d; DSA=%d; Databases=%d; Spring=%d",
                        user.getId(), user.getCourseProgress(Course.JAVA).getEarnedPoints(),
                        user.getCourseProgress(Course.DATA_STRUCTURES_AND_ALGORITHMS).getEarnedPoints(),
                        user.getCourseProgress(Course.DATABASES).getEarnedPoints(),
                        user.getCourseProgress(Course.SPRING).getEarnedPoints()
                );
                cli.send(response);
            }
        }
    }

    private void printStatistics() {
        cli.send("Type the name of a course to see details or 'back' to quit:");
        sendOverallStatistic();
        while (true) {
            String userInput = cli.receiveUserInput();
            if ("back".equals(userInput)) {
                break;
            } else {
                Course course = Course.getCourseByName(userInput);
                if (course == null) {
                    cli.send("Unknown course.");
                    continue;
                }
                CourseStatistic courseStatistic = statisticService.getCourseStatistic(course);
                cli.send(course.getName());
                cli.send("id     points completed");
                for (CourseUserStats userStat : courseStatistic.userStats()) {
                    double completePercentage = ((double) userStat.getPoints() / course.getPointsToFinish()) * 100;
                    BigDecimal bigDecimal = new BigDecimal(completePercentage).setScale(1, RoundingMode.HALF_UP);
                    cli.send("%-6s %-6s %s%%".formatted(userStat.getId(), userStat.getPoints(), bigDecimal.doubleValue()));
                }
            }
        }
    }

    private void sendOverallStatistic() {
        OverallStatistic stats = statisticService.getOverallStatistic();
        cli.send("Most popular: " + getPrintableCourseName(stats, Category.MOST_POPULAR));
        cli.send("Least popular: " + getPrintableCourseName(stats, Category.LEAST_POPULAR));
        cli.send("Highest activity: " + getPrintableCourseName(stats, Category.HIGHEST_ACTIVITY));
        cli.send("Lowest activity: " + getPrintableCourseName(stats, Category.LOWEST_ACTIVITY));
        cli.send("Easiest course: " + getPrintableCourseName(stats, Category.EASIEST_COURSE));
        cli.send("Hardest course: " + getPrintableCourseName(stats, Category.HARDEST_COURSE));
    }

    private String getPrintableCourseName(OverallStatistic stats, Category category) {
        String result = "n/a";
        List<Course> courses = stats.by(category);
        if (!courses.isEmpty()) {
            result = courses.stream().map(Course::getName).collect(Collectors.joining(", "));
        }
        return result;
    }

}
