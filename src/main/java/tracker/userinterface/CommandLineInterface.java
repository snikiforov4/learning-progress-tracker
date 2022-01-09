package tracker.userinterface;

import java.util.Scanner;

public class CommandLineInterface implements IUserInterface {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void send(String text) {
        System.out.println(text);
    }

    @Override
    public String receiveUserInput() {
        return scanner.nextLine().trim();
    }
}
