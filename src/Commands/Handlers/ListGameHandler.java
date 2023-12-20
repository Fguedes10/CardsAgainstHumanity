package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Game.Game;
import Server.Server;

import java.io.IOException;

/**
 * The `ListGameHandler` class implements the `CommandHandler` interface and handles the command for listing the currently running games.
 * It retrieves the list of running games from the server and sends the information to the client connection handler to be displayed to the client.
 */
public class ListGameHandler implements CommandHandler {

    /**
     * Executes the command for listing the currently running games. It retrieves the list of running games from the server and sends the
     * information to the client connection handler to be displayed to the client.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler to send the list of running games.
     * @throws IOException If an I/O error occurs while communicating with the client.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        clientConnectionHandler.writeMessage(Game.getRunningGames());
    }
}
