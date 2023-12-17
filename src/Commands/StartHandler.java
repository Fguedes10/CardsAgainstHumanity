package Commands;

import Client.ClientConnectionHandler;
import Messages.Messages;
import Server.Server;
import Game.Game;

import java.io.IOException;
import java.util.Optional;

public class StartHandler implements CommandHandler {

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException{

        String message = clientConnectionHandler.getMessage();

        if(message.split(" ").length < 2){
            clientConnectionHandler.send(Messages.START_INSTRUCTIONS);
            return;
        }
        Game game = Game.getGameByName(message.split(" ")[1]);
        assert game != null;
        if(clientConnectionHandler.getName().equals(game.owner.getName())){
            if(game != null){
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
