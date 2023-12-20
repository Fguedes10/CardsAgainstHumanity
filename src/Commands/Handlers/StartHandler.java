package Commands.Handlers;

import Client.ClientConnectionHandler;
import Commands.CommandHandler;
import Messages.Messages;
import Server.Server;
import Game.Game;

import java.io.IOException;
import java.util.Optional;


/**
 * The `StartHandler` class implements the `CommandHandler` interface and represents the handler for starting a game.
 * It checks if the client is the owner of the game, and starts the game if so.
 */
public class StartHandler implements CommandHandler {

    /**
     * Executes the command for starting a game.
     *
     * @param server                  The server instance.
     * @param clientConnectionHandler The client connection handler associated with the command.
     * @throws IOException If an I/O error occurs during game initialization or announcement.
     */
    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {

        String message = clientConnectionHandler.getMessage();

        if (message.split(" ").length < 2) {
            clientConnectionHandler.send(Messages.START_INSTRUCTIONS);
            return;
        }
        Game game = Game.getGameByName(message.split(" ")[1]);
        assert game != null;
        if (clientConnectionHandler.getName().equals(game.owner.getName())) {
            if (game != null) {
                try {
                    game.startGame(game);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            clientConnectionHandler.writeMessage(Messages.NOT_YOUR_GAME);
        }

        game.players.forEach(player -> player.setPlayingGame(game));
        game.players.forEach(player -> player.gameState = true);

    }
}
