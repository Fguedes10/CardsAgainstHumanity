package Commands;

import Client.ClientConnectionHandler;
import Game.Game;
import Messages.Messages;
import Server.Server;
import Server.ServerLauncher;

import java.io.IOException;

public class BuildHandler implements CommandHandler{

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        String name = clientConnectionHandler.askNameOfGame();
        int numOfPlayers = clientConnectionHandler.askNumberOfPlayers();
        Game game = new Game(clientConnectionHandler, numOfPlayers, name);
        clientConnectionHandler.setOwnedGame(game);
        clientConnectionHandler.writeMessage(Messages.GAME_BUILT + numOfPlayers + " players, called " + name + ".");
    }
}
