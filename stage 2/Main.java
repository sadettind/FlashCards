package flashcards;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        String str1 = scanner.nextLine();
        String str2 = scanner.nextLine();

        if (str2.equals(str1)) {
            System.out.println("Your answer is right!");
        } else
            System.out.println("Your answer is wrong...");

    }
}
