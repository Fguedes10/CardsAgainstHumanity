package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Server.Server;

import java.io.IOException;

/**
 * The `ListPlayersHandler` class implements the `CommandHandler` interface and handles the command for listing online players.
 * It retrieves the list of online players from the server through the client connection handler and sends the information to the client.
 */
public class ListPlayersHandler implements CommandHandler {

    /**
     * Executes the command for listing online players. It retrieves the list of online players from the server through the
     * client connection handler and sends the information to the client.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler to obtain the list of online players and send it to the client.
     * @throws IOException If an I/O error occurs while communicating with the client.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        clientConnectionHandler.writeMessage(clientConnectionHandler.listClients());
    }

}
