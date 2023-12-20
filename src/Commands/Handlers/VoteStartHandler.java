package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Messages.Messages;
import Server.Server;

import java.io.IOException;

/**
 * The `VoteStartHandler` class implements the `CommandHandler` interface and represents the handler for initiating
 * the voting phase of the game. It allows the owner of the game to start the voting process.
 */
public class VoteStartHandler implements CommandHandler {

    /**
     * Executes the command to start the voting phase of the game.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler associated with the command.
     * @throws IOException If an I/O error occurs while processing the command.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        if (!clientConnectionHandler.getName().equalsIgnoreCase(clientConnectionHandler.getPlayingGame().owner.getName())) {
            clientConnectionHandler.send("This game is not yours to control! Ask " + clientConnectionHandler.getPlayingGame().owner.getName() + " to start the voting phase!");
        }
        clientConnectionHandler.getPlayingGame().startVotingPhase(clientConnectionHandler);

    }
}
