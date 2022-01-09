package tracker.parser;

import tracker.model.UserCredentials;

public interface IUserCredentialsParser {
    UserCredentials parseFromString(String rawString);
}
