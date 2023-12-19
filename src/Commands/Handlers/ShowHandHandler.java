package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Server.Server;

import java.io.IOException;
import java.util.List;

public class ShowHandHandler implements CommandHandler {
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        List<String> playerHand = clientConnectionHandler.getCorrespondingClient().getCards();
        int index = 1;
        for (String card : playerHand) {
            try {
                clientConnectionHandler.writeMessage(index + " - " + card);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            index++;
        }

    }
}
