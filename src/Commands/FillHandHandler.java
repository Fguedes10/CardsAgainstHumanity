package Commands;

import Client.ClientConnectionHandler;
import Server.Server;
import Game.Game;

import java.io.IOException;

public class FillHandHandler implements CommandHandler{

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        ClientConnectionHandler owner = clientConnectionHandler.getPlayingGame().owner;
        clientConnectionHandler.getCorrespondingClient().fillHand();
        clientConnectionHandler.writeMessage("You have now picked cards.");
        clientConnectionHandler.getCorrespondingClient().getCards().forEach(card -> {
            try {
                clientConnectionHandler.writeMessage(card);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        owner.send(clientConnectionHandler.getName() + " has picked their hand!");
    }
}
