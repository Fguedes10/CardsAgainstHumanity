package Commands;

import Client.ClientConnectionHandler;
import Server.Server;

import java.io.IOException;

public class TurnHandler implements CommandHandler{

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        clientConnectionHandler.getPlayingGame().presentBlackCard();
    }
}
