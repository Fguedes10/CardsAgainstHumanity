package Commands;

import Client.ClientConnectionHandler;
import Server.Server;
import java.util.List;

import java.util.Objects;

import static java.lang.Integer.parseInt;

public class PlayCardHandler implements CommandHandler {

    @Override
    public void execute(Server server, ClientConnectionHandler clientConnectionHandler) {
        ClientConnectionHandler owner = clientConnectionHandler.getPlayingGame().owner;

        String message = clientConnectionHandler.getMessage();

        Integer playedNumber = parseInt(message.split(" ")[1]);

        String playedCard =  clientConnectionHandler.getCorrespondingClient().getCards().get(playedNumber -1);
        System.out.println(playedCard);

        owner.send(clientConnectionHandler.getName() + " has played their card!");

        clientConnectionHandler.getPlayingGame().setCardsInGame(playedCard);

        System.out.println(clientConnectionHandler.getPlayingGame().cardsInGame);

        clientConnectionHandler.getCorrespondingClient().cards.remove(playedCard);
        System.out.println(clientConnectionHandler.getCorrespondingClient().cards);

    }
}
