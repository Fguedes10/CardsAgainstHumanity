package Commands;

import Client.ClientConnectionHandler;
import Game.Game;
import Server.Server;

import java.io.IOException;

public class ListGameHandler implements CommandHandler{

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        clientConnectionHandler.writeMessage(Game.getRunningGames(clientConnectionHandler));
    }
}
