package Commands.Handlers;

import Client.Client;
import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Server.Server;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TurnHandler implements CommandHandler {

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        clientConnectionHandler.getPlayingGame().checkWinner();
        clientConnectionHandler.getPlayingGame().announceStartOfNewRound();

    }
}
