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

        try {
            Integer playedNumber = Integer.parseInt(message.split(" ")[1]);
            int indexToPlay = playedNumber - 1;

            List<String> playerCards = clientConnectionHandler.getCorrespondingClient().getCards();

            if (indexToPlay >= 0 && indexToPlay < playerCards.size()) {
                String playedCard = playerCards.get(indexToPlay);

                owner.send(clientConnectionHandler.getName() + " has played their card!");

                clientConnectionHandler.getPlayingGame().setCardsInGame(playedCard);

                System.out.println(clientConnectionHandler.getPlayingGame().cardsInGame);

                playerCards.remove(playedCard);
            } else {
                System.out.println("Please select a valid option from your hand");
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Please input a valid number. Please select an available card from your hand");
        }
    }
}