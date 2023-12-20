package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Game.Game;
import Messages.Messages;
import Server.Server;

import java.io.IOException;


/**
 * The `BuildHandler` class implements the `CommandHandler` interface and handles the /build command,
 * which is used to create a new game. It prompts the user for the game name and the number of players,
 * creates a new instance of the `Game` class, associates it with the client, and informs the client about
 * the successful creation of the game.
 */
public class BuildHandler implements CommandHandler {


    /**
     * Executes the /build command, creating a new game based on user input.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler associated with the user initiating the /build command.
     * @throws IOException If an I/O error occurs during the execution of the /build command.
     */

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        String name = clientConnectionHandler.askNameOfGame();
        int numOfPlayers = clientConnectionHandler.askNumberOfPlayers();
        Game game = new Game(clientConnectionHandler, numOfPlayers, name);
        clientConnectionHandler.setOwnedGame(game);
        clientConnectionHandler.writeMessage(Messages.GAME_BUILT + numOfPlayers + Messages.PLAYERS_CALLED + name + ".");
    }
}
