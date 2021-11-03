package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class FlashCards {

    Scanner scanner = new Scanner(System.in);
    Map<String, String> cards = new LinkedHashMap<>();
    Map<String, Integer> stats = new HashMap<>();

    ArrayList<String> list = new ArrayList<>();

    StringBuilder logData = new StringBuilder();
    private int hardestCount = 0;

    public void start() {
        while (true) {
            print("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String input = scanner.nextLine();
            scanInput(input);

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
                    print("Bye bye!");
                    return;
                case "log":
                    log();
                    break;
                case "hardest card":
                    hardestCard();
                    break;
                case "reset stats":
                    resetStats();
                    break;
            }
        }
    }

    private void print(String s) {
        if (logData.length() == 0)
            logData.append(s);
        else
            logData.append("\n").append(s);

        System.out.println(s);
    }

    private void scanInput(String s) {
        logData.append("\n").append(s);
    }

    private void hardestCard() {
        if (hardestCount == 0) {
            print("There are no cards with errors.");
            return;
        }

        for (var entry : stats.entrySet()) {
            if (entry.getValue() == hardestCount) {
                list.add(entry.getKey());
            }
        }
        if (list.size() == 1) {
            print("The hardest card is \"" + list.get(0) + "\". You have " + hardestCount + " errors answering it");
        } else {
            StringBuilder plural = new StringBuilder();
            plural.append("The hardest cards are");
            for (var entry : list) {
                plural.append(" \"").append(entry).append("\",");
            }
            plural.deleteCharAt(plural.length() - 1);
            plural.append(". You have ").append(hardestCount).append(" errors answering them.");
            print(plural.toString());
        }
    }

    private void resetStats() {
        stats.clear();
        hardestCount = 0;
        print("Card statistics have been reset.");
    }

    private void log() {
        print("File name:");
        String fileName = scanner.nextLine();
        scanInput(fileName);

        File file = new File(fileName);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(logData.toString());
            print("The log has been saved.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addCard() {
        print("The card:");
        String term = scanner.nextLine();
        scanInput(term);

        if (cards.containsKey(term)) {
            print("The card \"" + term + "\" already exists");
            return;
        }

        print("The definition of the card:");
        String definition = scanner.nextLine();
        scanInput(definition);

        if (cards.containsValue(definition)) {
            print("The definition \"" + definition + "\" already exists");
            return;
        }
        cards.put(term, definition);
        stats.put(term, 0);

        print("The pair (\"" + term + "\":\"" + definition + "\") has been added.");

    }

    private void removeCard() {
        print("Which card?");
        String selectCard = scanner.nextLine();
        scanInput(selectCard);

        if (cards.containsKey(selectCard)) {
            cards.remove(selectCard);
            stats.remove(selectCard);
            print("The card has been removed.");
        } else
            print("Can't remove \"" + selectCard + "\": there is no such card.");
    }

    private void importCard() {
        print("File name:");
        String fileName = scanner.nextLine();
        scanInput(fileName);
        File file = new File(fileName);

        try (Scanner readFile = new Scanner(file)) {
            int lineCount = readFile.nextInt();
            readFile.nextLine();
            for (int i = 0; i < lineCount; i++) {
                String[] arr = readFile.nextLine().split(" : ");
                cards.put(arr[0], arr[1]);
                stats.put(arr[0], Integer.parseInt(arr[2]));
                if (stats.get(arr[0]) > hardestCount) {
                    hardestCount = stats.get(arr[0]);
                }
            }
            print(lineCount + " cards have been loaded");
        } catch (FileNotFoundException e) {
            print("File not found");
        }
        printNewLine();

    }

    private void printNewLine() {
        logData.append("\n");
        System.out.println();
    }

    private void exportCard() {
        print("File name:");
        String fileName = scanner.nextLine();
        scanInput(fileName);
        File file = new File(fileName);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(cards.size());
            for (var card : cards.entrySet()) {
                writer.println(card.getKey() + " : " + card.getValue() + " : " + stats.get(card.getKey()));
            }
            print(cards.size() + " cards have been saved");
        } catch (FileNotFoundException e) {
            print("File not found");
        }


    }

    public void askCard() {
        print("How many times to ask?");
        int count = scanner.nextInt();
        scanInput(String.valueOf(count));
        scanner.nextLine();
        var itrCards = cards.entrySet().iterator();
        var itrStats = stats.entrySet().iterator();

        for (int i = 0; i < count; i++) {
            var entrySetCards = itrCards.next();
            var entrySetStats = itrStats.next();

            print("Print the definition of \"" + entrySetCards.getKey() + "\":");
            String answer = scanner.nextLine();
            scanInput(answer);


            if (cards.get(entrySetCards.getKey()).equals(answer)) {
                print("Correct!");
            } else {
                entrySetStats.setValue(entrySetStats.getValue() + 1);
                if (entrySetStats.getValue() > hardestCount) {
                    hardestCount = entrySetStats.getValue();
                }
                if (cards.containsValue(answer)) {
                    for (var entry : cards.entrySet()) {
                        if (entry.getValue().equals(answer)) {
                            print("Wrong. The right answer is \"" + cards.get(entrySetCards.getKey()) + "\", " +
                                    "but your definition is correct for \"" + entry.getKey() + "\".");
                        }
                    }
                } else {
                    print("Wrong. The right answer is \"" + cards.get(entrySetCards.getKey()) + "\".\n");
                }
            }

        }

    }
}




