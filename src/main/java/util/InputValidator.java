package util;

import java.util.InputMismatchException;
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
                    System.out.println("Out of bounds. Please enter an integer within the specified range.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input format. Please enter a valid integer.");
                scanner.nextLine();
            }
        }
        return value;
    }
    public String getValidString() {
        return scanner.nextLine();
    }

    public void closeScanner() {
        scanner.close();
    }
}
