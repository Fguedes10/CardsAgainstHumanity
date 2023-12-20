package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Messages.Messages;
import Server.Server;

import java.util.Optional;

/**
 * The `WhisperHandler` class implements the `CommandHandler` interface and represents the handler for sending
 * private messages (whispers) between clients. It processes the server and client connection handler to facilitate
 * private communication.
 */

public class WhisperHandler implements CommandHandler {
    /**
     * Executes the function by processing the given server and client connection handler to handle private messages.
     *
     * @param server                  The server object to execute the function on.
     * @param clientConnectionHandler The client connection handler to process.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) {
        String message = clientConnectionHandler.getMessage();

        if (message.split(" ").length < 3) {
            clientConnectionHandler.send(Messages.WHISPER_INSTRUCTIONS);
            return;
        }

        Optional<ClientConnectionHandler> receiverClient = server.getClientByName(message.split(" ")[1]);

        if (receiverClient.isEmpty()) {
            clientConnectionHandler.send(Messages.NO_SUCH_CLIENT);
            return;
        }

        String[] messageArray = message.split(" ");
        String messageToSend = "";

        for (int i = 2; i < messageArray.length; i++) {
            messageToSend += messageArray[i] + " ";
        }

        receiverClient.get().send(clientConnectionHandler.getName() + Messages.WHISPER + ": " + messageToSend);
    }
}
