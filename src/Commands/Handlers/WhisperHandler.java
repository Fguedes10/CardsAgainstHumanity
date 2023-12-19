package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
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
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) {
        String message = clientConnectionHandler.getMessage();

        if(message.split(" ").length < 3){
            clientConnectionHandler.send(Messages.WHISPER_INSTRUCTIONS);
            return;
        }

        Optional<ClientConnectionHandler> receiverClient = server.getClientByName(message.split(" ")[1]);

        if(receiverClient.isEmpty()){
            clientConnectionHandler.send(Messages.NO_SUCH_CLIENT);
            return;
        }

        String[] messageArray = message.split(" ");
        String messageToSend = "";

        for(int i = 2; i < messageArray.length; i++){
            messageToSend += messageArray[i] + " ";
        }

        receiverClient.get().send(clientConnectionHandler.getName() + Messages.WHISPER +  ": " + messageToSend);
    }
}
