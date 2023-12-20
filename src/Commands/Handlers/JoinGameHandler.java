package Commands.Handlers;

import Client.Client;
import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Messages.Messages;
import Server.Server;
import Game.Game;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The `JoinGameHandler` class implements the `CommandHandler` interface and handles the command for a client to join an existing game.
 * It checks if the join command includes the game name, adds the client to the specified game, and notifies the game owner when the
 * maximum number of players is reached, making the game ready to start.
 */
public class JoinGameHandler implements CommandHandler {

    /**
     * Executes the command for a client to join an existing game, adding the client to the specified game and notifying the game owner
     * when the maximum number of players is reached, making the game ready to start.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler associated with the joining client.
     * @throws IOException If an I/O error occurs while communicating with the client.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        String message = clientConnectionHandler.getMessage();

        if (message.split(" ").length < 2) {
            clientConnectionHandler.send(Messages.JOIN_INSTRUCTIONS);
            return;
        }

        Game game = Game.getGameByName(message.split(" ")[1]);

        assert game != null;
        game.join(clientConnectionHandler);

        if (game.checkAllPlayersInGame(game)) {
            game.owner.writeMessage(Messages.GAME_READY);
        }

    }


}
