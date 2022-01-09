package tracker.validate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import tracker.TestCases;
import tracker.model.UserCredentials;

public class UserCredentialsValidatorTest {

    private final IUserCredentialsValidator sut = new UserCredentialsValidator();

    @Test
    void checkCorrectCredentials() {
        for (Object[] testCase : TestCases.getCorrectCredentials()) {
            UserCredentials credentials = (UserCredentials) testCase[1];
            Assertions.assertThat(sut.validate(credentials)).isNotNull().satisfies(result ->
                    Assertions.assertThat(result.getStatus()).isEqualTo(ValidationStatus.OK));
        }
    }

    @Test
    void checkIncorrectCredentials() {
        for (Object[] testCase : TestCases.getIncorrectCredentials()) {
            UserCredentials credentials = (UserCredentials) testCase[1];
            ValidationStatus expectedStatus = (ValidationStatus) testCase[2];
            Assertions.assertThat(sut.validate(credentials)).isNotNull().satisfies(result ->
                    Assertions.assertThat(result.getStatus())
                            .describedAs("For credentials: %s", credentials)
                            .isEqualTo(expectedStatus));
        }
    }
}
