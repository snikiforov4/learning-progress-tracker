package tracker;

import tracker.model.UserCredentials;
import tracker.validate.ValidationStatus;

public final class TestCases {

    public static Object[][] getCorrectCredentials() {
        return new Object[][]{
                {"John Smith jsmith@hotmail.com", new UserCredentials("John", "Smith", "jsmith@hotmail.com")},
                {"Anny Doolittle anny.md@mail.edu", new UserCredentials("Anny", "Doolittle", "anny.md@mail.edu")},
                {"Jean-Claude O'Connor jcda123@google.net", new UserCredentials("Jean-Claude", "O'Connor", "jcda123@google.net")},
                {"Mary Emelianenko 125367at@zzz90.z9", new UserCredentials("Mary", "Emelianenko", "125367at@zzz90.z9")},
                {"Al Owen u15da125@a1s2f4f7.a1c2c5s4", new UserCredentials("Al", "Owen", "u15da125@a1s2f4f7.a1c2c5s4")},
                {"Robert Jemison Van de Graaff robertvdgraaff@mit.edu", new UserCredentials("Robert", "Jemison Van de Graaff", "robertvdgraaff@mit.edu")},
                {"Ed Eden a1@a1.a1", new UserCredentials("Ed", "Eden", "a1@a1.a1")},
                {"na'me s-u ii@ii.ii", new UserCredentials("na'me", "s-u", "ii@ii.ii")},
                {"n'a me su aa-b'b ab@ab.ab", new UserCredentials("n'a", "me su aa-b'b", "ab@ab.ab")},
                {"nA me 1@1.1", new UserCredentials("nA", "me", "1@1.1")},
        };
    }

    public static Object[][] getIncorrectCredentials() {
        return new Object[][]{
                {"", UserCredentials.invalid(), ValidationStatus.INVALID_CREDENTIALS},
                {" \t", UserCredentials.invalid(), ValidationStatus.INVALID_CREDENTIALS},
                {"name surname", UserCredentials.invalid(), ValidationStatus.INVALID_CREDENTIALS},
                {"n surname email@email.xyz", new UserCredentials("n", "surname", "email@email.xyz"), ValidationStatus.INVALID_FIRST_NAME},
                {"'name surname email@email.xyz", new UserCredentials("'name", "surname", "email@email.xyz"), ValidationStatus.INVALID_FIRST_NAME},
                {"-name surname email@email.xyz", new UserCredentials("-name",  "surname", "email@email.xyz"), ValidationStatus.INVALID_FIRST_NAME},
                {"name- surname email@email.xyz", new UserCredentials("name-", "surname", "email@email.xyz"), ValidationStatus.INVALID_FIRST_NAME},
                {"name' surname email@email.xyz", new UserCredentials("name'",  "surname", "email@email.xyz"), ValidationStatus.INVALID_FIRST_NAME},
                {"nam-'e surname email@email.xyz", new UserCredentials("nam-'e", "surname", "email@email.xyz"), ValidationStatus.INVALID_FIRST_NAME},
                {"na'-me surname email@email.xyz", new UserCredentials("na'-me", "surname", "email@email.xyz"), ValidationStatus.INVALID_FIRST_NAME},
                {"na--me surname email@email.xyz", new UserCredentials("na--me", "surname", "email@email.xyz"), ValidationStatus.INVALID_FIRST_NAME},
                {"na''me surname email@email.xyz", new UserCredentials("na''me", "surname", "email@email.xyz"), ValidationStatus.INVALID_FIRST_NAME},
                {"námé surname email@email.xyz", new UserCredentials("námé", "surname", "email@email.xyz"), ValidationStatus.INVALID_FIRST_NAME},
                {"name s email@email.xyz", new UserCredentials("name", "s", "email@email.xyz"), ValidationStatus.INVALID_LAST_NAME},
                {"name -surname email@email.xyz", new UserCredentials("name", "-surname", "email@email.xyz"), ValidationStatus.INVALID_LAST_NAME},
                {"name 'surname email@email.xyz", new UserCredentials("name", "'surname", "email@email.xyz"), ValidationStatus.INVALID_LAST_NAME},
                {"name surnam''e email@email.xyz", new UserCredentials("name", "surnam''e", "email@email.xyz"), ValidationStatus.INVALID_LAST_NAME},
                {"name surn--ame email@email.xyz", new UserCredentials("name", "surn--ame", "email@email.xyz"), ValidationStatus.INVALID_LAST_NAME},
                {"name s'-urname email@email.xyz", new UserCredentials("name", "s'-urname", "email@email.xyz"), ValidationStatus.INVALID_LAST_NAME},
                {"name su-'rname email@email.xyz", new UserCredentials("name",  "su-'rname", "email@email.xyz"), ValidationStatus.INVALID_LAST_NAME},
                {"name surname- email@email.xyz", new UserCredentials("name", "surname-", "email@email.xyz"), ValidationStatus.INVALID_LAST_NAME},
                {"name surname' email@email.xyz", new UserCredentials("name", "surname'", "email@email.xyz"), ValidationStatus.INVALID_LAST_NAME},
                {"name surnámé email@email.xyz", new UserCredentials("name", "surnámé", "email@email.xyz"), ValidationStatus.INVALID_LAST_NAME},
                {"name surname emailemail.xyz", new UserCredentials("name",  "surname", "emailemail.xyz"), ValidationStatus.INVALID_EMAIL},
                {"name surname email@emailxyz", new UserCredentials("name", "surname", "email@emailxyz"), ValidationStatus.INVALID_EMAIL},
                {"name surname email@e@mail.xyz", new UserCredentials("name", "surname", "email@e@mail.xyz"), ValidationStatus.INVALID_EMAIL},
        };
    }
}
