package Commands;

import Client.ClientConnectionHandler;
import Server.Server;
import java.util.List;

import java.io.IOException;
import java.util.Objects;

public class VoteHandler implements CommandHandler{

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) throws IOException {

       String playedCard =  clientConnectionHandler.getCorrespondingClient().getPlayedCard();

       clientConnectionHandler.getPlayingGame().getCardsInGame().stream()
               .filter(cards -> !cards.equalsIgnoreCase(playedCard))
               .forEach(clientConnectionHandler::send);

       Server.announceInGame("Here are the cards that are in game", clientConnectionHandler.getPlayingGame());

        for (int i = 0; i < clientConnectionHandler.getPlayingGame().cardsInGame.size(); i++) {
            Server.announceInGame(clientConnectionHandler.getPlayingGame().cardsInGame.get(i), clientConnectionHandler.getPlayingGame());
        }
       for (String card:clientConnectionHandler.getPlayingGame().getCardsInGame()){
           Server.announceInGame(card, clientConnectionHandler.getPlayingGame());
       }
       Server.announceInGame("Please vote with /vote <numberOfCard>", clientConnectionHandler.getPlayingGame());

       clientConnectionHandler.getCorrespondingClient().playerVote = clientConnectionHandler.getMessage().split(" ")[1];



    }
}
