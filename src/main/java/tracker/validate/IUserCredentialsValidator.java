package tracker.validate;

import tracker.model.UserCredentials;

public interface IUserCredentialsValidator {

    ValidationResult validate(UserCredentials userCredentials);

}
