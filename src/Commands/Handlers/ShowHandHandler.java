package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Game.Card;
import Server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The `ShowHandHandler` class implements the `CommandHandler` interface and represents the handler for showing the
 * hand of a player. It displays the cards in the player's hand on the server console.
 */
public class ShowHandHandler implements CommandHandler {

    /**
     * Executes the function by drawing and displaying a hand of cards for each client.
     *
     * @param server                  the server object
     * @param clientConnectionHandler the client connection handler object
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        List<String> cards = clientConnectionHandler.getCorrespondingClient().getCards();
        List<List<String>> cardLinesList = new ArrayList<>();
        int maxLines = 0;
        int index = 1;
        // Get lines for each card and find the max number of lines
        for (String card : cards) {
            List<String> cardLines = Card.drawHand(card, index++);

            cardLinesList.add(cardLines);
            maxLines = Math.max(maxLines, cardLines.size());
        }

        // Construct each line of the hand
        for (int i = 0; i < maxLines; i++) {
            StringBuilder handLine = new StringBuilder();
            for (List<String> cardLines : cardLinesList) {
                handLine.append(cardLines.size() > i ? cardLines.get(i) : " ".repeat(22)); // 22 is the card width
            }
            clientConnectionHandler.writeMessage(handLine.toString());
        }
    }
}
