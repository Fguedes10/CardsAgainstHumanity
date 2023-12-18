package Game;

import Messages.Messages;

import java.util.ArrayList;
import java.util.List;

public class Card {

    private String content;
    private int cardNumber;

    public Card(String content, int cardNumber) {
        this.content = content;
        this.cardNumber = cardNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public static List<String> splitIntoLines(String content, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = content.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() > maxWidth) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder();
            }
            if (currentLine.length() > 0) currentLine.append(" ");
            currentLine.append(word);
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    public static String drawBlackCard(String card) {
        StringBuilder cardBuilder = new StringBuilder();
        String topAndBottom = Messages.WHITE_BOLD + "+--------------------+" + Messages.RESET_COLOR; // Card top/bottom border (22 characters wide including +)
        int cardWidth = 18; // Width of the card content area (excluding borders)
        int totalLines = 6; // Total number of lines to be printed in the card
        String padding = Messages.WHITE_BOLD + "|                    |" + Messages.RESET_COLOR;

        // Splitting the content into multiple lines
        List<String> lines = splitIntoLines(card, cardWidth);

        cardBuilder.append(topAndBottom).append("\n");
        cardBuilder.append(padding).append("\n");

        // Building each line within the card
        for (int i = 0; i < totalLines; i++) {
            String line = i < lines.size() ? lines.get(i) : ""; // Get line from content or empty string
            int paddingLength = cardWidth - line.length();
            String paddedLine = Messages.WHITE_BOLD + "| " + line + " ".repeat(paddingLength) + " |" + Messages.RESET_COLOR;
            cardBuilder.append(paddedLine).append("\n");
        }

        // Re-print the padding line to fit the card width
        cardBuilder.append(padding).append("\n");
        cardBuilder.append(topAndBottom);

        return cardBuilder.toString();
    }


    public static List<String> drawWhiteCard(String card, int cardNumber) {
        List<String> cardLines = new ArrayList<>();
        String topAndBottom = Messages.WHITE_BACKGROUND + Messages.BLACK_BOLD + "+--------------------+" + Messages.RESET_COLOR; // Card top/bottom border (22 characters wide including +)
        int cardWidth = 18; // Width of the card content area (excluding borders)
        int totalLines = 6; // Total number of lines to be printed in the card
        String padding = Messages.WHITE_BACKGROUND + Messages.BLACK_BOLD + "| " + cardNumber + " ".repeat(cardWidth - 2 * String.valueOf(cardNumber).length()) + cardNumber + " |" + Messages.RESET_COLOR;

        // Splitting the content into multiple lines
        List<String> lines = splitIntoLines(card, cardWidth);

        // Adding the top border and padding line
        cardLines.add(topAndBottom);
        cardLines.add(padding);

        // Building each content line
        for (int i = 0; i < totalLines; i++) {
            String line = i < lines.size() ? lines.get(i) : ""; // Get line from content or empty string
            int paddingLength = cardWidth - line.length();
            String paddedLine = Messages.WHITE_BACKGROUND + Messages.BLACK_BOLD + "| " + line + " ".repeat(paddingLength) + " |" + Messages.RESET_COLOR;
            cardLines.add(paddedLine);
        }

        // Adding the padding line and bottom border
        cardLines.add(padding);
        cardLines.add(topAndBottom);

        return cardLines;
    }

    public static List<String> drawHand(String card, int cardNumber) {
        List<String> cardLines = new ArrayList<>();
        String topAndBottom = Messages.WHITE_BACKGROUND + Messages.BLACK_BOLD + "+--------------------+" + Messages.RESET_COLOR; // Card top/bottom border (22 characters wide including +)
        int cardWidth = 18; // Width of the card content area (excluding borders)
        int totalLines = 6; // Total number of lines to be printed in the card
        String padding = Messages.WHITE_BACKGROUND + Messages.BLACK_BOLD + "| " + cardNumber + " ".repeat(cardWidth - 2 * String.valueOf(cardNumber).length()) + cardNumber + " |" + Messages.RESET_COLOR;

        // Splitting the content into multiple lines
        List<String> lines = splitIntoLines(card, cardWidth);

        // Adding the top border and padding line
        cardLines.add(topAndBottom);
        cardLines.add(padding);

        // Building each content line
        for (int i = 0; i < totalLines; i++) {
            String line = i < lines.size() ? lines.get(i) : ""; // Get line from content or empty string
            int paddingLength = cardWidth - line.length();
            String paddedLine = Messages.WHITE_BACKGROUND + Messages.BLACK_BOLD + "| " + line + " ".repeat(paddingLength) + " |" + Messages.RESET_COLOR;
            cardLines.add(paddedLine);
        }

        // Adding the padding line and bottom border
        cardLines.add(padding);
        cardLines.add(topAndBottom);

        return cardLines;
    }

    public static void main(String[] args) {
        List<String> cardRepresentation = Card.drawHand("Sample card text", 1);

        for (String line : cardRepresentation) {
            System.out.println(line);
        }
    }

    // Additional methods if needed
}
