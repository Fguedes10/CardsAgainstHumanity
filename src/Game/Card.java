package Game;

import Messages.Messages;

import java.util.ArrayList;
import java.util.List;

import Client.ClientConnectionHandler;
import Messages.Messages;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The `Card` class represents a game card used in Cards Against Mindera.
 * It can be either a black card (prompt) or a white card (answer).
 */
public class Card {

    private String content;
    private int cardNumber;

    /**
     * Creates a new card with the specified content and card number.
     *
     * @param content    the content of the card
     * @param cardNumber the unique number assigned to the card
     */
    public Card(String content, int cardNumber) {
        this.content = content;
        this.cardNumber = cardNumber;
    }

    /**
     * Splits the content of a card into lines to fit within the specified maximum width.
     *
     * @param content  the content of the card
     * @param maxWidth the maximum width of each line
     * @return a list of lines after splitting the content
     */
    public static List<String> splitIntoLines(String content, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = content.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            // Check if adding this word exceeds the maxWidth
            if (currentLine.length() + word.length() + (!currentLine.isEmpty() ? 1 : 0) > maxWidth) {
                // Add the current line to lines and start a new line
                lines.add(currentLine.toString());
                currentLine = new StringBuilder();
            }
            // Add a space before the word if it's not the first word in the line
            if (!currentLine.isEmpty()) {
                currentLine.append(" ");
            }
            // Add the word to the current line
            currentLine.append(word);
        }

        // Add the last line if it's not empty
        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines;
    }


    /**
     * Draws a black card with a border and formatted content.
     *
     * @param card the content of the black card
     * @return a string representation of the drawn black card
     */

    public static String drawBlackCard(String card) {
        StringBuilder cardBuilder = new StringBuilder();
        String topAndBottom = Messages.WHITE_BOLD + "+------------------------+" + Messages.RESET_COLOR; // Card top/bottom border (22 characters wide including +)
        int cardWidth = 22; // Width of the card content area (excluding borders)
        int totalLines = 12; // Total number of lines to be printed in the card
        String padding = Messages.WHITE_BOLD + "|                        |" + Messages.RESET_COLOR;

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

    /**
     * Draws a white card with a border, card number, and formatted content.
     *
     * @param card       the content of the white card
     * @param cardNumber the unique number assigned to the card
     * @return a list of strings representing the drawn white card
     */
    public static List<String> drawWhiteCard(String card, int cardNumber) {
        List<String> cardLines = new ArrayList<>();
        String topAndBottom = Messages.WHITE_BACKGROUND + Messages.BLACK_BOLD + "+--------------------+" + Messages.RESET_COLOR; // Card top/bottom border (22 characters wide including +)
        int cardWidth = 22; // Width of the card content area (excluding borders)
        int totalLines = 12; // Total number of lines to be printed in the card
        int repeatCount = Math.max(0, cardWidth - 2 * String.valueOf(cardNumber).length());
        String padding = Messages.WHITE_BACKGROUND + Messages.BLACK_BOLD + "| " + cardNumber + " ".repeat(repeatCount) + cardNumber + " |" + Messages.RESET_COLOR;


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

    /**
     * Draws a hand card with a border, card number, and formatted content.
     *
     * @param card       the content of the hand card
     * @param cardNumber the unique number assigned to the card
     * @return a list of strings representing the drawn hand card
     */
    public static List<String> drawHand(String card, int cardNumber) {
        List<String> cardLines = new ArrayList<>();
        String topAndBottom = Messages.WHITE_BACKGROUND + Messages.BLACK_BOLD + "+------------------------+" + Messages.RESET_COLOR; // Card top/bottom border (22 characters wide including +)
        int cardWidth = 22; // Width of the card content area (excluding borders)
        int totalLines = 12; // Total number of lines to be printed in the card
        int repeatCount = Math.max(0, cardWidth - 2 * String.valueOf(cardNumber).length());
        String padding = Messages.WHITE_BACKGROUND + Messages.BLACK_BOLD + "| " + cardNumber + " ".repeat(repeatCount) + cardNumber + " |" + Messages.RESET_COLOR;


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

    /**
     * Prints the string representation of a card for testing purposes.
     *
     * @param card       the content of the card
     * @param cardNumber the unique number assigned to the card
     */
    public static void cardStringTester(String card) {
        drawHand(card, 1);
    }

    /**
     * Gets the content of the card.
     *
     * @return the content of the card
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the card.
     *
     * @param content the new content of the card
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the unique number assigned to the card.
     *
     * @return the card number
     */
    public int getCardNumber() {
        return cardNumber;
    }

    /**
     * Sets the unique number assigned to the card.
     *
     * @param cardNumber the new card number
     */
    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }


}