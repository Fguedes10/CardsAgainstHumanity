package Commands.Handlers;

import Client.Client;
import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Server.Server;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The `TurnHandler` class implements the `CommandHandler` interface and represents the handler for advancing the game turn.
 * It checks for a winner, announces the start of a new round, and prepares the game for the next turn.
 */

public class TurnHandler implements CommandHandler {
    /**
     * Executes the command for advancing the game turn.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler associated with the command.
     * @throws IOException If an I/O error occurs during winner checking or announcing the start of a new round.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        clientConnectionHandler.getPlayingGame().checkWinner();
        clientConnectionHandler.getPlayingGame().announceStartOfNewRound();

    }
}
