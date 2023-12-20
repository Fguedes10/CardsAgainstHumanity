package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Commands.LobbyCommands;
import Messages.Messages;
import Server.Server;

/**
 * The `ChangeNameHandler` class implements the `CommandHandler` interface and handles the /change_name command,
 * which allows a client to change their username. It extracts the new name from the command message, checks for
 * duplicates in the server, updates the client's name, and sends a response to the client regarding the name change.
 */
public class ChangeNameHandler implements CommandHandler {

    /**
     * Executes the /change_name command, allowing the client to change their username.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler associated with the user initiating the /change_name command.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) {
        String name = clientConnectionHandler
                .getMessage().split(" ")[1]
                .replace(LobbyCommands.CHANGE_NAME.getDescription(), "")
                .trim();
        if (server.getClientByName(name).isPresent()) {
            clientConnectionHandler.send(Messages.REPEATED_NAME);
            return;
        }
        clientConnectionHandler.setName(name);
        clientConnectionHandler.send(Messages.ACCEPT_NEW_NAME + name);
    }
}
