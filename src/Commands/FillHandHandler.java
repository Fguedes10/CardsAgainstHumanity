package Commands;

import Client.ClientConnectionHandler;
import Server.Server;

import java.io.IOException;
import java.util.List;

public class FillHandHandler implements CommandHandler {

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        ClientConnectionHandler owner = clientConnectionHandler.getPlayingGame().owner;
        clientConnectionHandler.getCorrespondingClient().fillHand();
        clientConnectionHandler.writeMessage("You have now picked cards.");
        List<String> cards = clientConnectionHandler.getCorrespondingClient().getCards();
        int index = 1;
        for (String card : cards) {
            try {
                clientConnectionHandler.writeMessage(index + " - " + card);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            index++;
        }
            owner.send(clientConnectionHandler.getName() + " has picked their hand!");
    }
}