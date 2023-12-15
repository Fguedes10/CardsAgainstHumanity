package Commands;

import Client.ClientConnectionHandler;
import Messages.Messages;
import Server.Server;
import Game.Game;

import java.io.IOException;

public class JoinGameHandler implements CommandHandler{

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {
        String message = clientConnectionHandler.getMessage();

        if(message.split(" ").length < 2){
            clientConnectionHandler.send(Messages.JOIN_INSTRUCTIONS);
            return;
        }

        Game game = Game.getGameByName(message.split(" ")[1]);

        assert game != null;
        game.join(clientConnectionHandler);

        if(game.checkAllPlayersInGame(game)){
            game.owner.writeMessage(Messages.GAME_READY);
        }

    }
}
