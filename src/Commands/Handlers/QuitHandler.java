package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Messages.Messages;
import Server.Server;


/**
 * The `QuitHandler` class implements the `CommandHandler` interface and represents the handler for quitting the game.
 * It removes the client from the server and broadcasts the quit message to all other clients.
 */
public class QuitHandler implements CommandHandler {


    /**
     * Executes the command for quitting the game. It removes the client from the server and broadcasts the quit message
     * to all other clients.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler associated with the command.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) {
        server.removeClient(clientConnectionHandler);
        Server.broadcast(clientConnectionHandler.getName(),
                clientConnectionHandler.getName() + Messages.CLIENT_DISCONNECTED);
    }
}
