package tracker.notify;

import lombok.AllArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import tracker.model.Course;
import tracker.model.User;
import tracker.userinterface.IUserInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@AllArgsConstructor
public class NotificationService implements INotificationService {

    private static final String TEXT_TEMPLATE = """
            To: %EMAIL_ADDRESS%
            Re: Your Learning Progress
            Hello, %FULL_USER_NAME%! You have accomplished our %COURSE_NAME% course!""";

    private final IUserInterface cli;
    private final BlockingDeque<NotificationEvent> storage = new LinkedBlockingDeque<>();

    @Override
    public void sendNotifications() {
        List<NotificationEvent> processingEvent = new ArrayList<>();
        storage.drainTo(processingEvent);
        for (NotificationEvent event : processingEvent) {
            String printableEvent = StringSubstitutor.replace(TEXT_TEMPLATE, Map.of(
                    "EMAIL_ADDRESS", event.email,
                    "FULL_USER_NAME", event.fullName,
                    "COURSE_NAME", event.courseName
            ), "%", "%");
            cli.send(printableEvent);
        }
        long totalNotifiedStudents = processingEvent.stream().map(NotificationEvent::email).distinct().count();
        cli.send("Total %d students have been notified.".formatted(totalNotifiedStudents));
    }

    @Override
    public void saveNotification(User user, Course course) {
        storage.addLast(new NotificationEvent(user.getFullName(), user.getEmail(), course.getName()));
    }

    private record NotificationEvent(String fullName, String email, String courseName) {
    }

}
