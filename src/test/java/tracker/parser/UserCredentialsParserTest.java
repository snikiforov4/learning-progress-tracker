package tracker.parser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import tracker.TestCases;
import tracker.model.UserCredentials;

class UserCredentialsParserTest {

    private final UserCredentialsParser sut = new UserCredentialsParser();

    @Test
    void checkCorrectCredentials() {
        for (Object[] testCase : TestCases.getCorrectCredentials()) {
            String rawString = (String) testCase[0];
            UserCredentials expected = (UserCredentials) testCase[1];
            Assertions.assertThat(sut.parseFromString(rawString)).satisfies(actual -> {
                Assertions.assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
                Assertions.assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
                Assertions.assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
            });
        }
    }

}