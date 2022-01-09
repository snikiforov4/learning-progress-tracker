package tracker.repository;

import tracker.model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryUserRepository implements IUserRepository {

    private final AtomicInteger userIdCnt = new AtomicInteger(0);
    private final Map<String, User> storage = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @Override
    public User save(User user) {
        String newUserId = String.valueOf(userIdCnt.incrementAndGet());
        user.setId(newUserId);
        storage.put(newUserId, user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public boolean existsByEmail(String email) {
        return emails.contains(email);
    }

}
