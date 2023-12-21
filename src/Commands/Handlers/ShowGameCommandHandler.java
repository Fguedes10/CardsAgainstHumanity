package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Game.Game;
import Messages.Messages;
import Server.Server;

import java.io.IOException;

public class ShowGameCommandHandler implements CommandHandler {
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        clientConnectionHandler.writeMessage(Messages.SHOW_GAME_COMMANDS);
    }
}
