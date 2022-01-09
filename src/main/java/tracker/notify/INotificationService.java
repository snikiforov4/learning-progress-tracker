package tracker.notify;

import tracker.model.Course;
import tracker.model.User;

public interface INotificationService {

    void sendNotifications();

    void saveNotification(User user, Course course);
}
