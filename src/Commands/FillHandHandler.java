package Commands;

import Client.ClientConnectionHandler;
import Game.Card;
import Server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FillHandHandler implements CommandHandler {

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
