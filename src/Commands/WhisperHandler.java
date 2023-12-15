package Commands;

import Messages.Messages;
import Server.Server;

import java.util.Optional;

public class WhisperHandler implements CommandHandler {
    /**
     * Executes the function by processing the given server and client connection handler.
     *
     * @param  server                  the server object to execute the function on
     * @param  clientConnectionHandler the client connection handler to process
     */
    @Override
    public void execute(Server server, Server.ClientConnectionHandler clientConnectionHandler) {
        String message = clientConnectionHandler.getMessage();

        if(message.split(" ").length < 3){
            clientConnectionHandler.send(Messages.WHISPER_INSTRUCTIONS);
            return;
        }

        Optional<Server.ClientConnectionHandler> receiverClient = server.getClientByName(message.split(" ")[1]);

        if(receiverClient.isEmpty()){
            clientConnectionHandler.send(Messages.NO_SUCH_CLIENT);
            return;
        }

        String messageToSend = message.substring(message.indexOf(" ") + 1).substring(message.indexOf(" ") + 1);
        receiverClient.get().send(clientConnectionHandler.getName() + Messages.WHISPER +  ": " + messageToSend);
    }
}
