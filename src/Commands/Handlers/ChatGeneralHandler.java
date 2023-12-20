package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Server.Server;

/**
 * The `ChatGeneralHandler` class implements the `CommandHandler` interface and handles general chat messages
 * sent by clients. However, the current implementation does not contain specific logic for processing general
 * chat messages, and the `execute` method is empty.
 */
public class ChatGeneralHandler implements CommandHandler {

    /**
     * Executes the handling of general chat messages. This implementation currently lacks specific logic for
     * processing general chat messages, and the method body is empty.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler associated with the user sending the chat message.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) {

    }
}
