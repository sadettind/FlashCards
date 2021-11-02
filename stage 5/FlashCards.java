package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class FlashCards {

    Scanner scanner = new Scanner(System.in);
    Map<String, String> cards = new LinkedHashMap<>();

    public void start() {
        while (true) {
            System.out.println("Input the action (add, remove, import, export, ask, exit):");
            String input = scanner.nextLine();
            
            switch (input) {
                case "add":
                    addCard();
                    break;
                case "remove":
                    removeCard();
                    break;
                case "import":
                    importCard();
                    break;
                case "export":
                    exportCard();
                    break;
                case "ask":
                    askCard();
                    break;
                case "exit":
                    System.out.println("Bye bye!");
                    return;
            }
        }
    }

    private void addCard() {
        System.out.println("The card:");
        String term = scanner.nextLine();

        if (cards.containsKey(term)) {
            System.out.printf("The card \"%s\" already exists\n", term);
            return;
        }

        System.out.println("The definition of the card:");
        String definition = scanner.nextLine();

        if (cards.containsValue(definition)) {
            System.out.printf("The definition \"%s\" already exists\n", definition);
            return;
        }
        cards.put(term, definition);
        System.out.printf("The pair (\"%s\":\"%s\") has been added.\n", term, definition);

    }

    private void removeCard() {
        System.out.println("Which card?");
        String selectCard = scanner.nextLine();
        if (cards.containsKey(selectCard)) {
            cards.remove(selectCard);
            System.out.println("The card has been removed.");
        } else
            System.out.printf("Can't remove \"%s\": there is no such card.", selectCard);
    }

    private void importCard() {
        System.out.println("File name:");
        File file = new File(scanner.nextLine());

        int lineCount = 0;
        try (Scanner readFile = new Scanner(file)) {
            lineCount = readFile.nextInt();
            readFile.nextLine();
            for (int i = 0; i < lineCount; i++) {
                String[] str = readFile.nextLine().split(" : ");
                cards.put(str[0], str[1]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        System.out.println(lineCount + " cards have been loaded");

    }

    private void exportCard() {
        System.out.println("File name:");
        File file = new File(scanner.nextLine());
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(cards.size());
            for (var card : cards.entrySet()) {
                writer.println(card.getKey() + " : " + card.getValue());
            }
            System.out.println(cards.size() + " cards have been saved");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }


    }

    public void askCard() {
        System.out.println("How many times to ask?");
        int count = scanner.nextInt();
        scanner.nextLine();
        var itr = cards.entrySet().iterator();

        for (int i = 0; i < count; i++) {
            var entrySet = itr.next();
            System.out.printf("Print the definition of \"%s\":\n", entrySet.getKey());
            String answer = scanner.nextLine();


            if (cards.get(entrySet.getKey()).equals(answer)) {
                System.out.println("Correct!");
            } else {
                if (cards.containsValue(answer)) {
                    for (var entry : cards.entrySet()) {
                        if (entry.getValue().equals(answer)) {
                            System.out.printf("Wrong. The right answer is \"%s\", " +
                                    "but your definition is correct for \"%s\".\n", cards.get(entrySet.getKey()), entry.getKey());
                        }
                    }
                } else {
                    System.out.printf("Wrong. The right answer is \"%s\".\n", cards.get(entrySet.getKey()));
                }
            }

        }

    }
}




