package tracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class UserCredentials {
    private static final UserCredentials INVALID = new UserCredentials(null, null, null);

    final String firstName;
    final String lastName;
    final String email;

    public static UserCredentials invalid() {
        return INVALID;
    }
}
