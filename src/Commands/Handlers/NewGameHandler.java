package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Server.Server;

/**
 * The `NewGameHandler` class implements the `CommandHandler` interface and represents the handler for creating a new game.
 * It currently doesn't perform any specific action when executed.
 */

public class NewGameHandler implements CommandHandler {

    /**
     * Executes the command for creating a new game. As of now, this handler does not perform any specific action.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler associated with the command.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) {

    }
}
