package util;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class InputValidator {
    private Scanner scanner;
    public InputValidator() {
        scanner = new Scanner(System.in);
    }
    public int getValidInteger(int from, int to) {
        int value;
        while (true) {
            try {
                value = scanner.nextInt();
                if (value >= from && value <= to) {
                    break;
                } else {
                    System.out.println("Please enter an integer within [" + from + ", " + to + "]");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input format. Please enter a valid integer.");
                scanner.nextLine();
            }
        }
        return value;
    }
    public String getValidString(List<String> validStrings) {
        String userInput;
        while (true) {
            userInput = scanner.next();
            if (validStrings != null && !validStrings.isEmpty()) {
                if (validStrings.contains(userInput)) {
                    break;
                } else {
                    System.out.println("-Invalid input. Please enter one of the valid options:");
                    System.out.print("-Valid options: ");
                    for (int i = 0; i < validStrings.size(); i++) System.out.print(validStrings.get(i) + " ");
                    System.out.println();
                }
            } else {
                break;
            }
        }
        return userInput;
    }

    public void closeScanner() {
        scanner.close();
    }
}
