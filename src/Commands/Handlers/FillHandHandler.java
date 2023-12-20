package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Game.Card;
import Server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The `FillHandHandler` class implements the `CommandHandler` interface and handles the command to fill a player's hand
 * with white cards at the beginning of a game turn. It informs the player that they have picked cards, displays the player's
 * hand, and notifies the game owner about the completion of the hand-picking process for the player.
 */
public class FillHandHandler implements CommandHandler {

    /**
     * Executes the command to fill a player's hand with white cards, notifies the player, displays the hand, and informs
     * the game owner about the completion of the hand-picking process for the player.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler associated with the player picking cards.
     * @throws IOException If an I/O error occurs while communicating with the client.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        ClientConnectionHandler owner = clientConnectionHandler.getPlayingGame().owner;
        clientConnectionHandler.getCorrespondingClient().fillHand();
        clientConnectionHandler.writeMessage("You have now picked cards.");

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

        owner.send(clientConnectionHandler.getName() + " has picked their hand!");
    }
}