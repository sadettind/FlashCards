package flashcards;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        Map<String, String> cards = new LinkedHashMap<>();

        System.out.println("Input the number of cards:");
        int numberOfCards = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numberOfCards; i++) {

            System.out.printf("Card #%d:\n", (i + 1));
            String term = scanner.nextLine();

            while (cards.containsKey(term)) {
                System.out.printf("The term \"%s\" already exists. Try again:\n", term);
                term = scanner.nextLine();
            }

            System.out.printf("The definition for card #%d:\n", (i + 1));
            String definition = scanner.nextLine();

            while (cards.containsValue(definition)) {
                System.out.printf("The definition \"%s\" already exists. Try again:\n", definition);
                definition = scanner.nextLine();
            }
            cards.put(term, definition);
        }

        for (var key : cards.keySet()) {

            System.out.printf("Print the definition of \"%s\":\n", key);
            String answer = scanner.nextLine();

            if (cards.get(key).equals(answer)) {
                System.out.println("Correct!");
            } else {
                if (cards.containsValue(answer)) {
                    for (var entry : cards.entrySet()) {
                        if (entry.getValue().equals(answer)) {
                            System.out.printf("Wrong. The right answer is \"%s\", " +
                                    "but your definition is correct for \"%s\".\n", cards.get(key), entry.getKey());
                        }
                    }
                } else {
                    System.out.printf("Wrong. The right answer is \"%s\".\n", cards.get(key));
                }
            }
        }
    }
}