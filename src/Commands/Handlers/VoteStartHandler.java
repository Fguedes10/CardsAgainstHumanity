package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Messages.Messages;
import Server.Server;

import java.io.IOException;

public class VoteStartHandler implements CommandHandler {

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        if(!clientConnectionHandler.getName().equalsIgnoreCase(clientConnectionHandler.getPlayingGame().owner.getName())){
            clientConnectionHandler.send("This game is not yours to control! Ask " + clientConnectionHandler.getPlayingGame().owner.getName() + " to start the voting phase!");
        }
            clientConnectionHandler.getPlayingGame().startVotingPhase(clientConnectionHandler);

    }
}
