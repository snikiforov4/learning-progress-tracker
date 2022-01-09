package tracker.parser;

import tracker.model.UserCredentials;

import java.util.Arrays;
import java.util.stream.Collectors;

public class UserCredentialsParser implements IUserCredentialsParser {
    @Override
    public UserCredentials parseFromString(String rawString) {
        String[] tokens = rawString.split("\\s+");
        if (tokens.length < 3) {
            return UserCredentials.invalid();
        }
        String firstName = tokens[0];
        String lastName = Arrays.stream(tokens, 1, tokens.length - 1)
                .collect(Collectors.joining(" "));
        String email = tokens[tokens.length - 1];
        return new UserCredentials(firstName, lastName, email);
    }
}
