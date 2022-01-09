package tracker.validate;

import tracker.model.UserCredentials;

import java.util.regex.Pattern;

public class UserCredentialsValidator implements IUserCredentialsValidator {

    private static final String NAME_REGEXP = "[a-zA-z][a-zA-Z-']*[a-zA-z]";
    private static final String NAME_SPECIAL_CHARS_REGEXP = ".*[-'][-'].*";
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
            // "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z0-9]{1,}$",
            Pattern.CASE_INSENSITIVE
    );

    @Override
    public ValidationResult validate(UserCredentials userCredentials) {
        return ValidationResult.fromStatus(check(userCredentials));
    }

    private ValidationStatus check(UserCredentials userCredentials) {
        if (userCredentials.getFirstName() == null || userCredentials.getLastName() == null
                || userCredentials.getEmail() == null) {
            return ValidationStatus.INVALID_CREDENTIALS;
        }
        if (isNameInvalid(userCredentials.getFirstName())) {
            return ValidationStatus.INVALID_FIRST_NAME;
        }
        for (String lastName : userCredentials.getLastName().split("\\s+")) {
            if (isNameInvalid(lastName)) {
                return ValidationStatus.INVALID_LAST_NAME;
            }
        }
        if (isEmailInvalid(userCredentials.getEmail())) {
            return ValidationStatus.INVALID_EMAIL;
        }
        return ValidationStatus.OK;
    }

    private boolean isNameInvalid(String name) {
        return !name.matches(NAME_REGEXP) || name.matches(NAME_SPECIAL_CHARS_REGEXP);
    }

    private boolean isEmailInvalid(String email) {
        return !VALID_EMAIL_ADDRESS_REGEX.matcher(email).find();
    }
}
