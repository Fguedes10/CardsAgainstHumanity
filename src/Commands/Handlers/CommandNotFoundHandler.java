package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Messages.Messages;
import Server.Server;

/**
 * The `CommandNotFoundHandler` class implements the `CommandHandler` interface and handles cases where an
 * unrecognized or invalid command is received. It responds to the client by sending a predefined error message
 * indicating that the command is not recognized.
 */
public class CommandNotFoundHandler implements CommandHandler {
    /**
     * Executes the handling of an unrecognized or invalid command by sending an error message to the client.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler associated with the user sending the invalid command.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) {
        clientConnectionHandler.send(Messages.NO_SUCH_COMMAND);
    }
}
